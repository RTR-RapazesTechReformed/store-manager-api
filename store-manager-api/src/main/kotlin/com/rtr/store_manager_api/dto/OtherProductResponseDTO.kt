package com.rtr.store_manager_api.dto

import com.rtr.store_manager_api.domain.enum.OtherProductType
import java.time.LocalDateTime

data class OtherProductResponseDTO(
    val id: String,
    val type: OtherProductType,
    val nationality: String?,
    val packageContents: String?,
    val extraInfo: String?,
    val createdBy: String?,
    val createdAt: LocalDateTime,
    val updatedBy: String?,
    val updatedAt: LocalDateTime,
    val deleted: Boolean
)
