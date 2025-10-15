package com.rtr.store_manager_api.dto

import java.time.LocalDateTime

data class StoreResponseDTO(
    val id: String,
    val name: String,
    val cep: String,
    val number: String,
    val complement: String?,
    val createdBy: String?,
    val updatedBy: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deleted: Boolean
)