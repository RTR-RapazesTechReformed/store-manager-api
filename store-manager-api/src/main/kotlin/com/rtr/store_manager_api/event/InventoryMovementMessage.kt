package com.rtr.store_manager_api.event

import com.rtr.store_manager_api.domain.enum.Operation
import java.math.BigDecimal
import java.time.LocalDateTime

data class InventoryMovementMessage(
    val movementId: String,
    val productId: String,
    val userId: String,
    val quantity: Int,
    val unitPurchasePrice: BigDecimal?,
    val unitSalePrice: BigDecimal?,
    val type: String,
    val operation: Operation,
    val description: String?,
    val createdBy: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)