package com.rtr.store_manager_api.dto.dashdto

data class TotalCardsInStockDTO(
    val totalCardsInStock: Long
)

data class TotalBoosterBoxesDTO(
    val totalBoosterBoxes: Long
)

interface TopPokemonByStockProjection {
    val pokemonName: String
    val totalQuantity: Long
}

data class TopCardKpiDTO(
    val nomeCarta: String,
    val quantidadeAtual: Long,
    val vendasUltimoMes: Long
)

data class TopCollectionKpiDTO(
    val colecao: String,
    val vendidasUltimoMes: Long,
    val estoqueAtual: Long
)




data class BoosterBoxesKpiDTO(
    val total: Long,
    val vendidasHoje: Long,
    val cadastradasHoje: Long
)

data class TopPokemonByStockDTO(
    val pokemonName: String,
    val totalQuantity: Long
)



interface SpendEarnByMonthProjection {
    fun getMonth(): String
    fun getTotalSpent(): Double
    fun getTotalEarned(): Double
}

data class CartasKpiDTO(
    val total: Long,
    val vendidasHoje: Long,
    val cadastradasHoje: Long
)
