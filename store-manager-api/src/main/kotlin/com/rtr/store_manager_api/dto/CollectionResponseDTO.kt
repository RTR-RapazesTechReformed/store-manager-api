package com.rtr.store_manager_api.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class CollectionResponseDTO(
    val id: String,
    val name: String,
    val abbreviation: String?,
    val releaseDate: LocalDate?,
    val createdBy: String?,
    val createdAt: LocalDateTime,
    val updatedBy: String?,
    val updatedAt: LocalDateTime,
    val deleted: Boolean
)