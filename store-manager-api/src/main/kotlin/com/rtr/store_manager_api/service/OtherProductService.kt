package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.OtherProductRequestDTO
import com.rtr.store_manager_api.dto.OtherProductResponseDTO

interface OtherProductService {
    fun createOtherProduct(dto: OtherProductRequestDTO, userId: String): OtherProductResponseDTO
    fun getAllOtherProducts(): List<OtherProductResponseDTO>
    fun getOtherProductById(id: String): OtherProductResponseDTO
    fun updateOtherProduct(id: String, dto: OtherProductRequestDTO, userId: String): OtherProductResponseDTO
    fun deleteOtherProduct(id: String, userId: String)
}