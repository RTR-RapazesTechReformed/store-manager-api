package com.rtr.store_manager_api.dto

data class UserResponseDTO(
    val id: String,
    val name: String,
    val email: String,
    val roleId: String,
    val createdAt: String?,
    val updatedAt: String?
)