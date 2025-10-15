package com.rtr.store_manager_api.dto

import jakarta.validation.constraints.*

data class UserUpdateDTO(
    @field:NotBlank(message = "Name cannot be blank when provided")
    val name: String? = null,

    @field:Email(message = "Invalid email format")
    val email: String? = null,

    @field:Size(min = 6, message = "Password must be at least 6 characters")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{6,}\$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    val password: String? = null,

    val storeId: String? = null,

    val roleName: String? = null
)

