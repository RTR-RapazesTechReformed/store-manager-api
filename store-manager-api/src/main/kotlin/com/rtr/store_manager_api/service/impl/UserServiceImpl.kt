package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.User
import com.rtr.store_manager_api.dto.UserRequestDTO
import com.rtr.store_manager_api.dto.UserUpdateDTO
import com.rtr.store_manager_api.dto.UserResponseDTO
import com.rtr.store_manager_api.repository.UserRepository
import com.rtr.store_manager_api.exception.ResourceNotFoundException
import com.rtr.store_manager_api.exception.RtrRuleException
import com.rtr.store_manager_api.repository.UserRoleRepository
import com.rtr.store_manager_api.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userRoleRepository: UserRoleRepository
) : UserService {

    override fun createUser(userInput: UserRequestDTO, userId: String): UserResponseDTO {
        if (userRepository.existsByEmail(userInput.email)) {
            throw RtrRuleException("E-mail já registrado: ${userInput.email}")
        }

        val role = userRoleRepository.findByName(userInput.roleName)
            ?: throw RtrRuleException("Cargo não encontrado: ${userInput.roleName}")

        val user = User(
            name = userInput.name,
            email = userInput.email,
            password = passwordEncoder.encode(userInput.password),
            role = role
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        return userRepository.save(user).toDTO()
    }

    override fun getAllUsers(username: String?, email: String?, role: String?): List<UserResponseDTO> {
        val users = userRepository.findAll()

        return users.filter { user ->
            (username == null || user.name.contains(username, ignoreCase = true)) &&
                    (email == null || user.email.contains(email, ignoreCase = true)) &&
                    (role == null || user.role.name.equals(role, ignoreCase = true))
        }.map { it.toDTO() }
    }

    override fun getUserById(id: String): UserResponseDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado com o id: $id") }
        return user.toDTO()
    }
    override fun updateUser(id: String, userInput: UserUpdateDTO, userId: String): UserResponseDTO {
        val existingUser = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado com o id: $id") }

        userInput.name?.let { existingUser.name = it }

        userInput.email?.let {
            if (it != existingUser.email && userRepository.existsByEmail(it)) {
                throw RtrRuleException("E-mail já registrado: $it")
            }
            existingUser.email = it
        }

        userInput.password?.let { existingUser.password = passwordEncoder.encode(it) }

        userInput.roleName?.let {
            existingUser.role = userRoleRepository.findByName(it)
                ?: throw RtrRuleException("Cargo não encontrado: $it")
        }
            ?: throw RtrRuleException("Cargo não encontrado: ${userInput.roleName}")

        existingUser.updatedAt = LocalDateTime.now()
        existingUser.updatedBy = userId

        return userRepository.save(existingUser).toDTO()
    }

    override fun deleteUser(id: String, userId: String): Boolean {
        val existingUser = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado com o id: $id") }

        existingUser.deleted = true
        existingUser.updatedAt = LocalDateTime.now()
        existingUser.updatedBy = userId

        userRepository.save(existingUser)
        return true
    }

    override fun validateUser(email: String, rawPassword: String): UserResponseDTO {
        val user = userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("Usuário não encontrado com o e-mail: $email")

        if (!passwordEncoder.matches(rawPassword, user.password)) {
            throw RtrRuleException("Senha inválida para o usuário $email")
        }

        return user.toDTO()
    }

    private fun User.toDTO(): UserResponseDTO {
        return UserResponseDTO(
            id = this.id,
            name = this.name,
            email = this.email,
            roleName = this.role.name,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            createdBy = this.createdBy,
            updatedBy = this.updatedBy,
            deleted = this.deleted
        )
    }
}
