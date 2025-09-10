package com.rtr.store_manager_api.dto

import java.time.LocalDateTime

data class UserRoleResponseDTO(
    val id: String,
    val name: String,
    val description: String,
    val permissions: Set<String> = emptySet(),
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)