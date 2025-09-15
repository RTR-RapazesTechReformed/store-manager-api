package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.InventoryMovementRequestDTO
import com.rtr.store_manager_api.dto.InventoryMovementResponseDTO

interface InventoryMovementService {
    fun createMovement(dto: InventoryMovementRequestDTO, userId: String): InventoryMovementResponseDTO
    fun getAllMovements(): List<InventoryMovementResponseDTO>
    fun getMovementById(id: String): InventoryMovementResponseDTO
    fun deleteMovement(id: String, userId: String)
}