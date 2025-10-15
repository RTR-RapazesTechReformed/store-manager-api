package com.rtr.store_manager_api.dto

import jakarta.validation.constraints.*

data class UserRequestDTO(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{6,}\$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    val password: String,

    val storeId: String? = null,

    @field:NotBlank(message = "Role name is required")
    val roleName: String
)