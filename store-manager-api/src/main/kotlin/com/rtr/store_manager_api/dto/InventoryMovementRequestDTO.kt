package com.rtr.store_manager_api.dto

import java.math.BigDecimal

data class InventoryMovementRequestDTO(
    val productId: String,
    val userId: String,
    val quantity: Int,
    val unitPurchasePrice: BigDecimal? = null,
    val unitSalePrice: BigDecimal? = null,
    val type: String,
    val description: String? = null
)