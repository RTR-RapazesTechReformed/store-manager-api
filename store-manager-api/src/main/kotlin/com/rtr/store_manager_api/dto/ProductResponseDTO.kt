package com.rtr.store_manager_api.dto

import com.rtr.store_manager_api.domain.enum.ProductCondition
import com.rtr.store_manager_api.domain.enum.ProductType
import java.time.LocalDateTime

data class ProductResponseDTO(
    val id: String,
    val name: String,
    val description: String?,
    val type: ProductType,
    val cardId: String?,
    val price: Double,
    val condition: ProductCondition,
    val createdBy: String?,
    val updatedBy: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deleted: Boolean
)