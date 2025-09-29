package com.rtr.store_manager_api.dto

data class CollectionWithCardsDTO(
    val id: String,
    val name: String,
    val abbreviation: String?,
    val releaseDate: String?,
    val generation: String?,
    val cards: List<CardSummaryDTO>
)