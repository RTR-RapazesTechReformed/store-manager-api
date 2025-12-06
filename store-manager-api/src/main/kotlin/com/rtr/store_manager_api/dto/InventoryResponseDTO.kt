package com.rtr.store_manager_api.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class InventoryResponseDTO(
    val productId: String,
    val productName: String,
    val quantity: Int,

    val storeName: String?,
    val productType: String,
    val condition: String?,
    val sellUnitPrice: BigDecimal?,
    val totalValue: BigDecimal?,

    val createdBy: String?,
    val updatedBy: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deleted: Boolean
)
