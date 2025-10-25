package com.rtr.store_manager_api.dto

import jakarta.validation.constraints.*

data class UserUpdateDTO(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val storeId: String? = null,
    val roleName: String? = null
)

