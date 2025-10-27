package com.rtr.store_manager_api.dto

import com.rtr.store_manager_api.domain.enum.ProductCondition
import java.math.BigDecimal

data class ProductRequestDTO(
    val name: String,
    val description: String? = null,
    val cardId: String? = null,
    val storeId: String,
    val otherProductId: String?,
    val otherProduct: OtherProductRequestDTO?,
    val price: BigDecimal,
    val condition: ProductCondition
)
