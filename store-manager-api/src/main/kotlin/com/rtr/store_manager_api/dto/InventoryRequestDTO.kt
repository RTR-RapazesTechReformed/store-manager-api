package com.rtr.store_manager_api.dto

import jakarta.validation.constraints.Positive


data class InventoryRequestDTO(
    val productId: String?,
    @field:Positive(message = "Quantity must be positive")
    val quantity: Int
)