package com.rtr.store_manager_api.dto.dashdto

import java.time.LocalDateTime

data class MonthlyAcquisitionDTO(
    val month: String,
    val movementId: String,
    val description: String?,
    val quantity: Int,
    val unitPurchasePrice: Double,
    val totalCost: Double,
    val createdAt: LocalDateTime
)

data class SalesOverviewDTO(
    val productName: String,
    val salePrice: Double,
    val totalSold: Long,
    val totalRevenue: Double,
    val monthYear: String
)

data class StockAgingOverviewDTO(
    val productId: String,
    val productName: String,
    val productDescription: String?,
    val currentPrice: Double,
    val conditionName: String,
    val currentQuantity: Int,
    val productCreatedAt: LocalDateTime,
    val lastMovementDate: LocalDateTime,
    val daysInStock: Long,
    val totalMovements: Long,
    val totalIn: Long,
    val totalOut: Long,
    val balance: Long
)

data class ValuedCardDTO(
    val productId: String,
    val productName: String,
    val productDescription: String?,
    val avgSalePrice: Double,
    val maxSalePrice: Double,
    val minSalePrice: Double,
    val currentSalePrice: Double,
    val differenceFromAvg: Double,
    val percentageChange: Double,
    val currentStock: Int,
    val lastSale: LocalDateTime
)
