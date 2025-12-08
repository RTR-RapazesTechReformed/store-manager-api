package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.dashdto.*
import java.time.LocalDate
import java.time.LocalDateTime

interface DashService {

    // === KPIs ===
    fun getTotalCardsInStock(): TotalCardsInStockDTO
    fun getTotalBoosterBoxes(): TotalBoosterBoxesDTO
    fun getTopPokemonByStock(): TopPokemonByStockDTO
    fun getCartasKpi(): CartasKpiDTO
    fun getBoosterBoxesKpi(): BoosterBoxesKpiDTO
    fun getTopCardKpi(): TopCardKpiDTO
    fun getTopCollectionKpi(): TopCollectionKpiDTO

    // === Gráficos existentes ===
    fun getMonthlyInvestments(): List<MonthlyInvestmentDTO>
    fun getSalesOverview(): List<SalesOverviewDTO>
    fun getStockAgingOverview(): List<StockAgingOverviewDTO>
    fun getValuedCards(): List<ValuedCardDTO>

    // === Novos gráficos ===
    fun getHistoricalDistribution(date: LocalDateTime): List<InventoryDistributionDTO>

    fun getCardSales(start: LocalDate, end: LocalDate): List<TopSellingCardDTO>

    fun getProfitByCategory(start: LocalDate, end: LocalDate): List<ProfitByCategoryProjection>

     fun getSpendVsEarn(start: LocalDate, end: LocalDate): List<SpendEarnByMonthProjection>

    fun getStockValuation(start: LocalDate, end: LocalDate): List<StockValuationProjection>
}
