package com.rtr.store_manager_api.dto

data class InventoryMovementResponse(
    val movementId: String,
    val status: String = "PENDING",
    val message: String? = null
)
