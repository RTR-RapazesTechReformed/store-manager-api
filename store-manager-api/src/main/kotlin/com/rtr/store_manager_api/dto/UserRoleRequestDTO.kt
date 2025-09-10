package com.rtr.store_manager_api.dto

data class UserRoleRequestDTO(
    val id: String,
    val name: String,
    val description: String,
    val permissions: Set<String> = emptySet()
)