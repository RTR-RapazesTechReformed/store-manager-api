package com.rtr.store_manager_api.event

import java.time.LocalDateTime

data class InventoryMovementErrorMessage(
    val movementId: String,
    val productId: String,
    val userId: String,
    val quantity: Int,
    val type: String,
    val errorMessage: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
