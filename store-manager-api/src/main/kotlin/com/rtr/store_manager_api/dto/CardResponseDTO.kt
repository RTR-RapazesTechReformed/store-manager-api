package com.rtr.store_manager_api.dto

import java.time.LocalDateTime

data class CardResponseDTO(
    val id: String,
    val title: String,
    val artistName: String?,
    val season: String?,
    val collectionId: String,
    val code: String,
    val rarity: String,
    val createdBy: String?,
    val createdAt: LocalDateTime?,
    val updatedBy: String?,
    val updatedAt: LocalDateTime?,
    val delete: Boolean
)