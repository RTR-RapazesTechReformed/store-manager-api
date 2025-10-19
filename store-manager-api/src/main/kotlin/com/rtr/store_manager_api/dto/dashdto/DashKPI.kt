package com.rtr.store_manager_api.dto.dashdto

data class TotalCardsInStockDTO(
    val totalCardsInStock: Long
)

data class TotalBoosterBoxesDTO(
    val totalBoosterBoxes: Long
)

data class TopPokemonByStockDTO(
    val pokemonName: String,
    val totalQuantity: Long
)

data class TopCollectionByItemsDTO(
    val collectionName: String,
    val totalItems: Long
)