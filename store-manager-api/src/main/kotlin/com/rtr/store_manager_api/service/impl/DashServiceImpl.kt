package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.repository.DashRepository
import com.rtr.store_manager_api.service.DashService
import org.springframework.stereotype.Service

@Service
class DashServiceImpl(
    private val dashRepository: DashRepository
) : DashService {

    // === KPIs ===
    override fun getTotalCardsInStock(): Long {
        return dashRepository.getTotalCardsInStock()
    }

    override fun getTotalBoosterBoxes(): Long {
        return dashRepository.getTotalBoosterBoxes()
    }

    override fun getTopPokemonByStock(): Map<String, Any> {
        return dashRepository.getTopPokemonByStock()
    }

    override fun getTopCollectionByItems(): Map<String, Any> {
        return dashRepository.getTopCollectionByItems()
    }

    // === Gráficos ===
    override fun getMonthlyAcquisitions(): List<Map<String, Any>> {
        return dashRepository.getMonthlyAcquisitions()
    }

    override fun getSalesOverview(): List<Map<String, Any>> {
        return dashRepository.findSalesOverview()
    }

    override fun getStockAgingOverview(): List<Map<String, Any>> {
        return dashRepository.findStockAgingOverview()
    }

    override fun getValuedCards(): List<Map<String, Any>> {
        return dashRepository.findValuedCards()
    }
}
