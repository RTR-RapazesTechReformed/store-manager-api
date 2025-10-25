package com.rtr.store_manager_api.service
import com.rtr.store_manager_api.dto.dashdto.*


interface DashService {

    fun getTotalCardsInStock(): TotalCardsInStockDTO
    fun getTotalBoosterBoxes(): TotalBoosterBoxesDTO
    fun getTopPokemonByStock(): TopPokemonByStockDTO
    fun getTopCollectionByItems(): TopCollectionByItemsDTO

    fun getMonthlyAcquisitions(): List<MonthlyAcquisitionDTO>
    fun getSalesOverview(): List<SalesOverviewDTO>
    fun getStockAgingOverview(): List<StockAgingOverviewDTO>
    fun getValuedCards(): List<ValuedCardDTO>
}
