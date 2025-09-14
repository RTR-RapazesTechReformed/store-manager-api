package com.rtr.store_manager_api.dto

import com.rtr.store_manager_api.domain.enum.ProductCondition
import com.rtr.store_manager_api.domain.enum.ProductType

data class ProductRequestDTO(
    val name: String,
    val description: String? = null,
    val type: ProductType,
    val cardId: String? = null,
    val price: Double,
    val condition: ProductCondition
)
