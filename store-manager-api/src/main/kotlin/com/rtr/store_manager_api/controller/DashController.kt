package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.service.DashService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashController(
    private val dashService: DashService
) {

    // === KPIs ===

    @GetMapping("/total-cards-in-stock")
    fun getTotalCardsInStock(): ResponseEntity<Long> {
        val result = dashService.getTotalCardsInStock()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/total-booster-boxes")
    fun getTotalBoosterBoxes(): ResponseEntity<Long> {
        val result = dashService.getTotalBoosterBoxes()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/top-pokemon-by-stock")
    fun getTopPokemonByStock(): ResponseEntity<Map<String, Any>> {
        val result = dashService.getTopPokemonByStock()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/top-collection-by-items")
    fun getTopCollectionByItems(): ResponseEntity<Map<String, Any>> {
        val result = dashService.getTopCollectionByItems()
        return ResponseEntity.ok(result)
    }

    // === Gráficos ===

    @GetMapping("/monthly-acquisitions")
    fun getMonthlyAcquisitions(): ResponseEntity<List<Map<String, Any>>> {
        val result = dashService.getMonthlyAcquisitions()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/sales-overview")
    fun getSalesOverview(): ResponseEntity<List<Map<String, Any>>> {
        val result = dashService.getSalesOverview()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/stock-aging-overview")
    fun getStockAgingOverview(): ResponseEntity<List<Map<String, Any>>> {
        val result = dashService.getStockAgingOverview()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/valued-cards")
    fun getValuedCards(): ResponseEntity<List<Map<String, Any>>> {
        val result = dashService.getValuedCards()
        return ResponseEntity.ok(result)
    }
}
