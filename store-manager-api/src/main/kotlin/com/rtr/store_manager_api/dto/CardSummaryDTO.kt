package com.rtr.store_manager_api.dto

data class CardSummaryDTO(
    val id: String,
    val title: String,
    val pokemonType: String?,
    val code: String,
    val rarity: String,
    val nationality: String?
)