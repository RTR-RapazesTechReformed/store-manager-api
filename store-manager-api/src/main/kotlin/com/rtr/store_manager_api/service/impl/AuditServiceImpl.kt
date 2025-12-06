package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.InventoryAudit
import com.rtr.store_manager_api.domain.entity.InventoryMovement
import com.rtr.store_manager_api.domain.entity.Product
import com.rtr.store_manager_api.domain.enum.AuditStatus
import com.rtr.store_manager_api.domain.enum.Operation
import com.rtr.store_manager_api.dto.InventoryAuditResponseDTO
import com.rtr.store_manager_api.repository.InventoryAuditRepository
import com.rtr.store_manager_api.service.AuditService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuditServiceImpl(
    private val inventoryAuditRepository: InventoryAuditRepository
) : AuditService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun logMovement(
        product: Product,
        movement: InventoryMovement,
        userId: String,
        quantityBefore: Int,
        quantityAfter: Int,
        status: AuditStatus,
        operation: Operation,
        description: String?,
        errorMessage: String?
    ): InventoryAudit {
        val audit = InventoryAudit(
            product = product,
            movement = movement,
            userId = userId,
            movementType = movement.type.name,
            quantity = movement.quantity,
            quantityBefore = quantityBefore,
            quantityAfter = quantityAfter,
            timestamp = LocalDateTime.now(),
            description = description,
            errorMessage = errorMessage,
            operation = operation,
            status = status
        )

        val saved = inventoryAuditRepository.save(audit)

        when (status) {
            AuditStatus.PROCESSED -> {
                logger.info(
                    "Auditoria registrada (SUCESSO): Movimento ${movement.id} - " +
                            "Produto ${product.id} - Quantidade ${movement.quantity}"
                )
            }
            AuditStatus.FAILED -> {
                logger.error(
                    "Auditoria registrada (ERRO): Movimento ${movement.id} - " +
                            "Erro: $errorMessage"
                )
            }
            AuditStatus.PENDING -> {
                logger.warn("Auditoria registrada (PENDENTE): Movimento ${movement.id}")
            }
        }

        return saved
    }

    override fun getErrorLogs(): List<InventoryAudit> {
        return inventoryAuditRepository.findByStatusOrderByTimestampDesc(AuditStatus.FAILED)
    }

    override fun getErrorLogsByDate(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<InventoryAudit> {
        return inventoryAuditRepository.findByTimestampBetween(startDate, endDate)
            .filter { it.status == AuditStatus.FAILED }
    }

    override fun getSuccessLogs(): List<InventoryAudit> {
        return inventoryAuditRepository.findByStatusOrderByTimestampDesc(AuditStatus.PROCESSED)
    }

    override fun getAllMovements(): List<InventoryAuditResponseDTO> {
        return inventoryAuditRepository.findAllWithNames().map { row -> toDTO(row) }
    }

    override fun getMovementById(id: String): InventoryAuditResponseDTO {
        val row = inventoryAuditRepository.findByIdWithNames(id)
            ?: throw RuntimeException("Auditoria não encontrada: $id")
        return toDTO(row)
    }

   fun toDTO(row: Map<String, Any>) =
        InventoryAuditResponseDTO(
            id = row["id"].toString(),
            productName = row["product_name"]?.toString() ?: "Desconhecido",
            userName = row["user_name"]?.toString() ?: "Sistema",

            movementType = row["movement_type"].toString(),
            quantity = (row["quantity"] as Number).toInt(),
            quantityBefore = (row["quantity_before"] as Number).toInt(),
            quantityAfter = (row["quantity_after"] as Number).toInt(),

            unitPurchasePrice = (row["unit_purchase_price"] as? Number)?.toDouble(),
            unitSalePrice = (row["unit_sale_price"] as? Number)?.toDouble(),

            timestamp = row["timestamp"].toString(),
            operation = row["operation"].toString(),
            status = row["status"].toString(),
            description = row["description"]?.toString(),
            errorMessage = row["error_message"]?.toString()
            )
}