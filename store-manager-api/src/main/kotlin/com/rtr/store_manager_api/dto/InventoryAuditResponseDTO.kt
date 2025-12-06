package com.rtr.store_manager_api.dto

data class InventoryAuditResponseDTO(
    val id: String,
    val productName: String,
    val userName: String,

    val movementType: String,
    val quantity: Int,
    val quantityBefore: Int,
    val quantityAfter: Int,

    val unitPurchasePrice: Double?,
    val unitSalePrice: Double?,

    val timestamp: String,
    val operation: String,
    val status: String,
    val description: String?,
    val errorMessage: String?,
)

