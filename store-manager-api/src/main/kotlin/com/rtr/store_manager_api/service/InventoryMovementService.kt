package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.InventoryMovementCreatedResponse
import com.rtr.store_manager_api.dto.InventoryMovementRequestDTO
import com.rtr.store_manager_api.dto.InventoryMovementResponseDTO
import com.rtr.store_manager_api.dto.InventoryMovementUpdateDTO

interface InventoryMovementService {
    fun createMovement(dto: InventoryMovementRequestDTO, userId: String): InventoryMovementCreatedResponse
    fun getAllMovements(): List<InventoryMovementResponseDTO>
    fun getMovementById(id: String): InventoryMovementResponseDTO
    fun updateMovement(id: String, dto: InventoryMovementUpdateDTO, userId: String): InventoryMovementResponseDTO
    fun deleteMovement(id: String, userId: String)
}