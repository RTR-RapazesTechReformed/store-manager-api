package com.rtr.store_manager_api.dto

data class InventoryMovementRequestDTO(
    val productId: String,
    val userId: String,
    val quantity: Int,
    val unitPurchasePrice: Double? = null,
    val unitSalePrice: Double? = null,
    val type: String,
    val description: String? = null
)