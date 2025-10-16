package com.rtr.store_manager_api.dto

data class InventoryMovementCreatedResponse(
    val movementId: String,
    val status: String = "PENDING",
    val message: String? = null
)
