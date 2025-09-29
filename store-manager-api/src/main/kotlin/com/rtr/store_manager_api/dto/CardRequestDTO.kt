package com.rtr.store_manager_api.dto

data class CardRequestDTO(
    val title: String,
    val season: String?,
    val pokemonType: String,
    val nationality: String?,
    val collectionId: String,
    val code: String,
    val rarity: String
)
