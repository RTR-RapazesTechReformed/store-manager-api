package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.User
import com.rtr.store_manager_api.dto.UserRequestDTO
import com.rtr.store_manager_api.dto.UserUpdateDTO
import com.rtr.store_manager_api.dto.UserResponseDTO
import com.rtr.store_manager_api.repository.UserRepository
import com.rtr.store_manager_api.exception.ResourceNotFoundException
import com.rtr.store_manager_api.exception.RtrRuleException
import com.rtr.store_manager_api.repository.StoreRepository
import com.rtr.store_manager_api.repository.UserRoleRepository
import com.rtr.store_manager_api.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userRoleRepository: UserRoleRepository,
    private val storeRepository: StoreRepository,
) : UserService {

    override fun createUser(userInput: UserRequestDTO, userId: String?): UserResponseDTO {
        if (userRepository.existsByEmail(userInput.email)) {
            throw RtrRuleException("E-mail já registrado: ${userInput.email}")
        }

        val role = userRoleRepository.findByName(userInput.roleName)
            ?: throw RtrRuleException("Cargo não encontrado: ${userInput.roleName}")

        val store = userInput.storeId?.let {
            storeRepository.findByIdAndDeletedFalse(it).orElseThrow { RtrRuleException("Loja não encontrada: $it") }
        }

        val user = User(
            name = userInput.name,
            email = userInput.email,
            password = passwordEncoder.encode(userInput.password),
            role = role,
            store = store
        ).apply {
            createdBy = userId ?: "system"
            updatedBy = userId ?: "system"
        }

        return userRepository.save(user).toDTO()
    }

    override fun getAllUsers(username: String?, email: String?, storeId: String?, role: String?): List<UserResponseDTO> {
        val users = userRepository.findAllByDeletedFalse()

        return users.filter { user ->
            (username == null || user.name.contains(username, ignoreCase = true)) &&
                    (email == null || user.email.contains(email, ignoreCase = true)) &&
                    (role == null || user.role.name.equals(role, ignoreCase = true)) &&
                    (storeId == null || user.store?.id == storeId)
        }.map { it.toDTO() }
    }

    override fun getUserById(id: String): UserResponseDTO {
        val user = userRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado com o id: $id") }
        return user.toDTO()
    }

    override fun updateUser(id: String, userInput: UserUpdateDTO, userId: String): UserResponseDTO {
        val existingUser = userRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado com o id: $id") }

        userInput.name?.let { name ->
            require(name.isNotBlank()) { "Nome não pode estar vazio" }
            require(name.length in 2..200) { "Nome deve ter entre 2 e 200 caracteres" }
            existingUser.name = name.trim()
        }

        userInput.email?.let { email ->
            require(email.isNotBlank()) { "E-mail não pode estar vazio" }
            require(email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))) {
                "Formato de e-mail inválido"
            }

            val normalizedEmail = email.trim().lowercase()

            if (normalizedEmail != existingUser.email.lowercase() && userRepository.existsByEmail(normalizedEmail)) {
                throw RtrRuleException("E-mail inválido: $normalizedEmail")
            }

            existingUser.email = normalizedEmail
        }

        userInput.password?.let { password ->
            require(password.isNotBlank()) { "Senha não pode estar vazia" }
            require(password.length >= 6) { "Senha deve ter no mínimo 6 caracteres" }
            require(password.matches(
                Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{6,}\$")
            )) {
                "Senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial (@\$!%*?&)"
            }

            existingUser.password = passwordEncoder.encode(password)
        }

        userInput.roleName?.let { roleName ->
            require(roleName.isNotBlank()) { "Nome do cargo não pode estar vazio" }

            existingUser.role = userRoleRepository.findByName(roleName)
                ?: throw RtrRuleException("Cargo não encontrado: $roleName")
        }

        if (userInput.storeId != null) {
            if (userInput.storeId.isBlank()) {
                existingUser.store = null
            } else {
                require(userInput.storeId.length == 36) { "ID da loja deve ser um UUID válido" }

                val store = storeRepository.findByIdAndDeletedFalse(userInput.storeId)
                    .orElseThrow { RtrRuleException("Loja não encontrada: ${userInput.storeId}") }

                existingUser.store = store
            }
        }

        existingUser.updatedAt = LocalDateTime.now()
        existingUser.updatedBy = userId

        return userRepository.save(existingUser).toDTO()
    }

    override fun deleteUser(id: String, userId: String): Boolean {
        val existingUser = userRepository.findByIdAndDeletedFalse(id)
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
            storeId = this.store?.id,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            createdBy = this.createdBy,
            updatedBy = this.updatedBy,
            deleted = this.deleted
        )
    }
}
