package com.rtr.store_manager_api.dto

import com.rtr.store_manager_api.domain.enum.ProductCondition

data class ProductRequestDTO(
    val name: String,
    val description: String? = null,
    val cardId: String? = null,
    val otherProductId: String? = null,
    val price: Double,
    val condition: ProductCondition
)
