package com.rtr.store_manager_api.dto

import java.time.LocalDate


data class CollectionRequestDTO(
    val name: String,
    val abbreviation: String?,
    val releaseDate: LocalDate?
)
