package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.domain.entity.Permission
import com.rtr.store_manager_api.dto.PermissionRequestDTO
import com.rtr.store_manager_api.dto.PermissionResponseDTO
import com.rtr.store_manager_api.exception.ResourceNotFoundException
import com.rtr.store_manager_api.repository.PermissionRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PermissionServiceImpl(
    private val permissionRepository: PermissionRepository
) : PermissionService {

    override fun createPermission(dto: PermissionRequestDTO, userId: String): PermissionResponseDTO {
        val permission = Permission(
            name = dto.name,
            description = dto.description
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        return permissionRepository.save(permission).toDTO()
    }

    override fun getAllPermissions(): List<PermissionResponseDTO> =
        permissionRepository.findAll().filter { !it.deleted }.map { it.toDTO() }

    override fun getPermissionById(id: String): PermissionResponseDTO {
        val permission = permissionRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Permissão não encontrada: $id") }
        return permission.toDTO()
    }

    override fun updatePermission(id: String, dto: PermissionRequestDTO, userId: String): PermissionResponseDTO {
        val existing = permissionRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Permissão não encontrada: $id") }

        existing.name = dto.name
        existing.description = dto.description
        existing.updatedAt = LocalDateTime.now()
        existing.updatedBy = userId

        return permissionRepository.save(existing).toDTO()
    }

    override fun deletePermission(id: String, userId: String): Boolean {
        val existing = permissionRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Permissão não encontrada: $id") }

        existing.deleted = true
        existing.updatedAt = LocalDateTime.now()
        existing.updatedBy = userId
        permissionRepository.save(existing)

        return true
    }

    private fun Permission.toDTO(): PermissionResponseDTO =
        PermissionResponseDTO(
            id = id,
            name = name,
            description = description,
            createdAt = createdAt,
            updatedAt = updatedAt,
            createdBy = createdBy,
            updatedBy = updatedBy,
            delete = deleted
        )
}
