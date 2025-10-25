package com.rtr.store_manager_api.event

import com.rtr.store_manager_api.domain.enum.Operation
import java.time.LocalDateTime

data class InventoryMovementErrorMessage(
    val movementId: String,
    val productId: String,
    val userId: String,
    val quantity: Int,
    val type: String,
    val operation: Operation,
    val errorMessage: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)