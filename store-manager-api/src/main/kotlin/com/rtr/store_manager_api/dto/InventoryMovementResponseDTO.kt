package com.rtr.store_manager_api.dto

import java.time.LocalDateTime

data class InventoryMovementResponseDTO(
    val id: String,
    val productId: String,
    val userId: String,
    val quantity: Int,
    val unitPurchasePrice: Double?,
    val unitSalePrice: Double?,
    val type: String,
    val description: String?,
    val createdBy: String?,
    val createdAt: LocalDateTime,
    val updatedBy: String?,
    val updatedAt: LocalDateTime,
    val deleted: Boolean
)
