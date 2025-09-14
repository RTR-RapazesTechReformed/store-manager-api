package com.rtr.store_manager_api.dto

data class InventoryRequestDTO(
    val productId: String,
    val quantity: Int
)