package com.rtr.store_manager_api.dto

import java.time.LocalDateTime

data class UserResponseDTO(
    val id: String,
    val name: String,
    val email: String,
    val roleName: String,
    val createdBy: String?,
    val updatedBy: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deleted: Boolean
)