package com.rtr.store_manager_api.dto

import java.time.LocalDateTime

data class CardResponseDTO(
    val id: String,
    val title: String,
    val season: String?,
    val pokemonType: String,
    val collectionId: String,
    val code: String,
    val rarity: String,
    val nationality: String?,
    val createdBy: String?,
    val createdAt: LocalDateTime?,
    val updatedBy: String?,
    val updatedAt: LocalDateTime?,
    val deleted: Boolean
)