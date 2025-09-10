package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.UserRoleRequestDTO
import com.rtr.store_manager_api.dto.UserRoleResponseDTO

interface UserRoleService {
    fun createRole(dto: UserRoleRequestDTO, userId: String): UserRoleResponseDTO
    fun getAllRoles(): List<UserRoleResponseDTO>
    fun getRoleById(id: String): UserRoleResponseDTO
    fun updateRole(id: String, dto: UserRoleRequestDTO, userId: String): UserRoleResponseDTO
    fun deleteRole(id: String, userId: String): Boolean
}
