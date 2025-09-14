package com.rtr.store_manager_api.dto

import com.rtr.store_manager_api.domain.entity.Permission
import java.time.LocalDateTime

data class UserRoleResponseDTO(
    val id: String,
    val name: String,
    val description: String,
    val permissions: MutableSet<Permission> = mutableSetOf(),
    val createdBy: String?,
    val updatedBy: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val delete: Boolean
)