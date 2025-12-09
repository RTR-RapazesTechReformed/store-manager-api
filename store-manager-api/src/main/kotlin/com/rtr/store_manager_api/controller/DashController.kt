package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.dashdto.*
import com.rtr.store_manager_api.service.DashService
import com.rtr.store_manager_api.service.impl.DashServiceImpl
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/store-manager-api/dashboard")
class DashController(
    private val dashService: DashService,
) {

    // === KPIs ===

    @GetMapping("/total-cards-in-stock")
    fun getTotalCardsInStock(): ResponseEntity<TotalCardsInStockDTO> {
        val result = dashService.getTotalCardsInStock()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/kpis-cartas")
    fun getKpis() = dashService.getCartasKpi()

    @GetMapping("/kpis-boosters")
    fun getBoosterBoxesKpis(): BoosterBoxesKpiDTO {
        return dashService.getBoosterBoxesKpi()
    }
    @GetMapping("/kpi-top-card")
    fun getTopCardKpi(): TopCardKpiDTO {
        return dashService.getTopCardKpi()
    }

    @GetMapping("/total-booster-boxes")
    fun getTotalBoosterBoxes(): ResponseEntity<TotalBoosterBoxesDTO> {
        val result = dashService.getTotalBoosterBoxes()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/top-pokemon-by-stock")
    fun getTopPokemonByStock(): ResponseEntity<TopPokemonByStockDTO> {
        val result = dashService.getTopPokemonByStock()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/kpi-top-collection")
    fun getTopCollectionKpi(): ResponseEntity<TopCollectionKpiDTO> {
        val result = dashService.getTopCollectionKpi()
        return ResponseEntity.ok(result)
    }


    // === GRÁFICOS ===

    @GetMapping("/monthly-investments")
    fun getMonthlyAcquisitions(): ResponseEntity<List<MonthlyInvestmentDTO>> {
        val result = dashService.getMonthlyInvestments()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/sales-overview")
    fun getSalesOverview(): ResponseEntity<List<SalesOverviewDTO>> {
        val result = dashService.getSalesOverview()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/stock-aging-overview")
    fun getStockAgingOverview(): ResponseEntity<List<StockAgingOverviewDTO>> {
        val result = dashService.getStockAgingOverview()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/valued-cards")
    fun getValuedCards(): ResponseEntity<List<ValuedCardDTO>> {
        val result = dashService.getValuedCards()
        return ResponseEntity.ok(result)
    }


        @GetMapping("/distribution/historical")
        fun getHistoricalDistribution(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            date: LocalDateTime
        ): List<InventoryDistributionDTO> {

            return dashService.getHistoricalDistribution(date)
        }

        @GetMapping("/sales")
        fun getCardSales(
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            start: LocalDate,

            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            end: LocalDate
        ): List<TopSellingCardDTO> {
            return dashService.getCardSales(start, end)
        }

        @GetMapping("/profit")
        fun getProfitByCategory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            start: LocalDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            end: LocalDate
        ): List<ProfitByCategoryProjection> {

            return dashService.getProfitByCategory(start, end)
        }


        @GetMapping("/spend-earn")
        fun getSpendVsEarn(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            start: LocalDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            end: LocalDate
        ): List<SpendEarnByMonthProjection> {

            return dashService.getSpendVsEarn(start, end)
        }

        @GetMapping("/valuation")
        fun getStockValuation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            start: LocalDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            end: LocalDate
        ): List<StockValuationProjection> {
            return dashService.getStockValuation(start, end)
        }
}
