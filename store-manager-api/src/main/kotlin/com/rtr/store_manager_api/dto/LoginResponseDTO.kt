package com.rtr.store_manager_api.dto

data class LoginResponseDTO(
    val sessionId: String,
    val userId: String,
    val active: Boolean
)