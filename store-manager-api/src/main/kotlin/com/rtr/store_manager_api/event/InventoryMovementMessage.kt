package com.rtr.store_manager_api.event

import java.math.BigDecimal

data class InventoryMovementMessage(
    val movementId: String,
    val productId: String,
    val userId: String,
    val quantity: Int,
    val unitPurchasePrice: BigDecimal?,
    val unitSalePrice: BigDecimal?,
    val type: String,
    val description: String?,
    val createdBy: String
)