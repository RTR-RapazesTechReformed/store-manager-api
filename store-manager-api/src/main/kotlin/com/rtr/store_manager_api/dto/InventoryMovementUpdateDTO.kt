package com.rtr.store_manager_api.dto

import java.math.BigDecimal

data class InventoryMovementUpdateDTO(
    val unitPurchasePrice: BigDecimal? = null,
    val unitSalePrice: BigDecimal? = null,
    val description: String? = null
)