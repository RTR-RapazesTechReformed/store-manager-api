package com.rtr.store_manager_api.dto

import com.rtr.store_manager_api.domain.enum.OtherProductType

data class OtherProductRequestDTO(
    val type: OtherProductType,
    val nationality: String? = null,
    val packageContents: String? = null,
    val extraInfo: String? = null
)