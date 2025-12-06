package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.domain.entity.InventoryAudit
import com.rtr.store_manager_api.domain.entity.InventoryMovement
import com.rtr.store_manager_api.domain.entity.Product
import com.rtr.store_manager_api.domain.enum.AuditStatus
import com.rtr.store_manager_api.domain.enum.Operation
import com.rtr.store_manager_api.dto.InventoryAuditResponseDTO
import java.time.LocalDateTime

interface AuditService {
    fun logMovement(
        product: Product,
        movement: InventoryMovement,
        userId: String,
        quantityBefore: Int,
        quantityAfter: Int,
        status: AuditStatus,
        operation: Operation,
        description: String? = null,
        errorMessage: String? = null
    ): InventoryAudit
    fun getErrorLogs(): List<InventoryAudit>
    fun getErrorLogsByDate(startDate: LocalDateTime, endDate: LocalDateTime): List<InventoryAudit>
    fun getSuccessLogs(): List<InventoryAudit>
    fun getAllMovements(): List<InventoryAuditResponseDTO>
    fun getMovementById(id: String): InventoryAuditResponseDTO
}