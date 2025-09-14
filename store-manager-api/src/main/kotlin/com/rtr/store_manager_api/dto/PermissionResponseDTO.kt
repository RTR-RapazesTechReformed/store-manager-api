package com.rtr.store_manager_api.dto

import java.time.LocalDateTime

data class PermissionResponseDTO (
    val id: String? = null,
    val name: String,
    val description: String,
    val createdBy: String?,
    val updatedBy: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val delete: Boolean
)
