package com.rtr.store_manager_api.service

interface DashService {

    // === KPIs ===
    fun getTotalCardsInStock(): Long
    fun getTotalBoosterBoxes(): Long
    fun getTopPokemonByStock(): Map<String, Any>
    fun getTopCollectionByItems(): Map<String, Any>

    // === Gráficos ===
    fun getMonthlyAcquisitions(): List<Map<String, Any>>
    fun getSalesOverview(): List<Map<String, Any>>
    fun getStockAgingOverview(): List<Map<String, Any>>
    fun getValuedCards(): List<Map<String, Any>>
}
