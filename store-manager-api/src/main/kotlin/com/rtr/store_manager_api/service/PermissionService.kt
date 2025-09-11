package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.PermissionRequestDTO
import com.rtr.store_manager_api.dto.PermissionResponseDTO

interface PermissionService {
    fun createPermission(dto: PermissionRequestDTO, userId: String): PermissionResponseDTO
    fun getAllPermissions(): List<PermissionResponseDTO>
    fun getPermissionById(id: String): PermissionResponseDTO
    fun updatePermission(id: String, dto: PermissionRequestDTO, userId: String): PermissionResponseDTO
    fun deletePermission(id: String, userId: String): Boolean
}