package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.InventoryRequestDTO
import com.rtr.store_manager_api.dto.InventoryResponseDTO

interface InventoryService {
    fun createInventory(dto: InventoryRequestDTO, userId: String): InventoryResponseDTO
    fun getAllInventory(): List<InventoryResponseDTO>
    fun getInventoryByProductId(productId: String): InventoryResponseDTO
    fun updateInventory(productId: String, dto: InventoryRequestDTO, userId: String): InventoryResponseDTO
    fun deleteInventory(productId: String, userId: String)
}