package com.rtr.store_manager_api.dto

import java.time.LocalDateTime

data class InventoryResponseDTO(
    val productId: String,
    val productName: String?,
    val quantity: Int,
    val createdBy: String?,
    val updatedBy: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deleted: Boolean
)