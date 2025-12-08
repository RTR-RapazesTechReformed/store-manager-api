package com.rtr.store_manager_api.dto.dashdto

import java.math.BigDecimal
import java.time.LocalDateTime

// ---------- PROJECTION PARA O REPOSITORY ----------
interface MonthlyAcquisitionProjection {
    fun getMonth(): String
    fun getMovementId(): String
    fun getDescription(): String?
    fun getQuantity(): Int
    fun getUnitPurchasePrice(): BigDecimal
    fun getTotalCost(): BigDecimal
    fun getCreatedAt(): LocalDateTime
}

interface TopSellingCardProjection {
    fun getProductName(): String
    fun getTotalSold(): Long
}

interface ProfitByCategoryProjection {
    fun getCategory(): String
    fun getTotalProfit(): Double
}

data class TopSellingCardDTO(
    val productName: String,
    val totalSold: Long
)

// ---------- DTOs USADOS NA CONTROLLER ----------
data class MonthlyInvestmentDTO(
    val month: String,
    val productId: String,
    val productName: String,
    val totalQuantity: Int,
    val unitPrice: BigDecimal,
    val totalInvested: BigDecimal
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

data class InventoryDistributionDTO(
    val category: String,
    val totalQuantity: Long
)

interface StockValuationProjection {
    fun getMonth(): String
    fun getTotalStockValue(): Double
}
