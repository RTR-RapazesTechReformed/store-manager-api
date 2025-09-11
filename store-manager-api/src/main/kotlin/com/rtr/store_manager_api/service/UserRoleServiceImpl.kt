package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.domain.entity.Permission
import com.rtr.store_manager_api.domain.entity.UserRole
import com.rtr.store_manager_api.dto.UserRoleRequestDTO
import com.rtr.store_manager_api.dto.UserRoleResponseDTO
import com.rtr.store_manager_api.exception.ResourceNotFoundException
import com.rtr.store_manager_api.exception.RtrRuleException
import com.rtr.store_manager_api.repository.PermissionRepository
import com.rtr.store_manager_api.repository.UserRepository
import com.rtr.store_manager_api.repository.UserRoleRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UserRoleServiceImpl(
    private val userRoleRepository: UserRoleRepository,
    private val userRepository: UserRepository,
    private val permissionRepository: PermissionRepository
) : UserRoleService {

    override fun createRole(dto: UserRoleRequestDTO, userId: String): UserRoleResponseDTO {
        val creator = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User-id enviado na header não encontrado: $userId") }

        val permissions: MutableSet<Permission> = dto.permissions
            .map { permId ->
                permissionRepository.findById(permId)
                    .orElseThrow { ResourceNotFoundException("Permissão não encontrada: $permId") }
            }
            .toMutableSet()

        val role = UserRole(
            id = UUID.randomUUID().toString(),
            name = dto.name,
            description = dto.description,
            permissions = permissions,
        ).apply {
            createdBy = creator.id
            updatedBy = creator.id
        }

        val saved = userRoleRepository.save(role)
        return saved.toDTO()
    }

    override fun getAllRoles(): List<UserRoleResponseDTO> {
        return userRoleRepository.findAll()
            .filter { !it.deleted }
            .map { it.toDTO() }
    }

    override fun getRoleById(id: String): UserRoleResponseDTO {
        val role = userRoleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Cargo não encontrado com o id: $id") }
        if (role.deleted) throw ResourceNotFoundException("Cargo já removido")
        return role.toDTO()
    }

    override fun updateRole(id: String, dto: UserRoleRequestDTO, userId: String): UserRoleResponseDTO {
        val role = userRoleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Cargo não encontrado com o id: $id") }

        val updater = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User-id enviado na header não encontrado: $userId") }

        role.name = dto.name
        role.description = dto.description
        role.updatedBy = updater.id
        role.updatedAt = LocalDateTime.now()

        return userRoleRepository.save(role).toDTO()
    }

    override fun deleteRole(id: String, userId: String): Boolean {
        val role = userRoleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Cargo não encontrado com o id: $id") }

        val deleter = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User-id enviado na header não encontrado: $userId") }

        val usersWithRole = userRepository.existsByRoleIdAndDeletedFalse(role.id)
        if (usersWithRole) {
            throw RtrRuleException("Não é possível deletar a role '${role.name}' pois ela está sendo utilizada por usuários ativos")
        }

        role.deleted = true
        role.updatedBy = deleter.id
        role.updatedAt = LocalDateTime.now()

        userRoleRepository.save(role)
        return true
    }

    private fun UserRole.toDTO(): UserRoleResponseDTO {
        return UserRoleResponseDTO(
            id = this.id,
            name = this.name,
            description = this.description,
            permissions = this.permissions,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

}
