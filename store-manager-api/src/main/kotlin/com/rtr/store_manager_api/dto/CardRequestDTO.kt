package com.rtr.store_manager_api.dto

data class CardRequestDTO(
    val title: String,
    val artistName: String?,
    val season: String?,
    val collectionId: String,
    val code: String,
    val rarity: String
)
