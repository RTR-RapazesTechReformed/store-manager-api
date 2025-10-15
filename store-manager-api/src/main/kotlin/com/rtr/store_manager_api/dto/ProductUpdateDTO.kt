package com.rtr.store_manager_api.dto

import com.rtr.store_manager_api.domain.enum.ProductCondition
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class ProductUpdateDTO(

    @field:Size(min = 1, message = "Name cannot be blank when provided")
    val name: String? = null,

    val description: String? = null,

    val storeId : String? = null,

    @field:DecimalMin(value = "0.0", message = "Price must be positive")
    val price: BigDecimal? = null,

    val condition: ProductCondition? = null
)