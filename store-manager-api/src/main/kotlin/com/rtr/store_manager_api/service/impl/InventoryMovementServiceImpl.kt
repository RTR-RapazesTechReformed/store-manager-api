package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.InventoryMovement
import com.rtr.store_manager_api.domain.enum.MovementType
import com.rtr.store_manager_api.domain.enum.Operation
import com.rtr.store_manager_api.dto.InventoryMovementRequestDTO
import com.rtr.store_manager_api.dto.InventoryMovementResponse
import com.rtr.store_manager_api.dto.InventoryMovementResponseDTO
import com.rtr.store_manager_api.dto.InventoryMovementUpdateDTO
import com.rtr.store_manager_api.event.InventoryMovementMessage
import com.rtr.store_manager_api.event.InventoryProducer
import com.rtr.store_manager_api.repository.InventoryMovementRepository
import com.rtr.store_manager_api.repository.InventoryRepository
import com.rtr.store_manager_api.repository.ProductRepository
import com.rtr.store_manager_api.repository.UserRepository
import com.rtr.store_manager_api.service.InventoryMovementService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class InventoryMovementServiceImpl(
    private val inventoryMovementRepository: InventoryMovementRepository,
    private val inventoryRepository: InventoryRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val producer: InventoryProducer
) : InventoryMovementService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createMovement(
        dto: InventoryMovementRequestDTO,
        userId: String
    ): InventoryMovementResponse {
        logger.info("Criando novo movimento de estoque para produto: ${dto.productId}")

        val product = productRepository.findById(dto.productId)
            .orElseThrow { NoSuchElementException("Produto ${dto.productId} não encontrado") }

        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("Usuário $userId não encontrado") }

        val movementId = UUID.randomUUID().toString()

        logger.info("Enviando mensagem CREATE para Kafka: Movimento $movementId")

        producer.sendMovementMessage(
            InventoryMovementMessage(
                movementId = movementId,
                productId = product.id,
                userId = user.id,
                quantity = dto.quantity,
                type = dto.type.uppercase(),
                operation = Operation.CREATE,
                description = dto.description,
                createdBy = userId,
                unitPurchasePrice = dto.unitPurchasePrice,
                unitSalePrice = dto.unitSalePrice
            )
        )

        logger.info("Mensagem CREATE enviada para fila: $movementId com status PENDING")

        return InventoryMovementResponse(
            movementId = movementId,
            status = "PENDING"
        )
    }

    override fun getAllMovements(): List<InventoryMovementResponseDTO> {
        logger.info("Consultando todos os movimentos de estoque")
        return inventoryMovementRepository.findAll()
            .filter { !it.deleted }
            .map { it.toResponseDTO() }
    }

    override fun getMovementById(id: String): InventoryMovementResponseDTO {
        logger.info("Consultando movimento: $id")
        return inventoryMovementRepository.findById(id)
            .orElseThrow { NoSuchElementException("Movimento $id não encontrado") }
            .toResponseDTO()
    }

    override fun updateMovement(
        id: String,
        dto: InventoryMovementUpdateDTO,
        userId: String
    ): InventoryMovementResponse {
        logger.info("Processando UPDATE para movimento: $id")

        val existingMovement = inventoryMovementRepository.findById(id)
            .orElseThrow { NoSuchElementException("Movimento $id não encontrado") }

        if (existingMovement.deleted) {
            logger.warn("Tentativa de atualizar movimento deletado: $id")
            throw IllegalArgumentException("Não é permitido atualizar movimento deletado")
        }

        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("Usuário $userId não encontrado") }

        logger.info("Validação OK. Enviando mensagem UPDATE para Kafka: $id")

        producer.sendMovementMessage(
            InventoryMovementMessage(
                movementId = id,
                productId = existingMovement.product.id,
                userId = existingMovement.user.id,
                quantity = existingMovement.quantity,
                type = existingMovement.type.toString(),
                operation = Operation.UPDATE,
                description = dto.description ?: existingMovement.description,
                createdBy = userId,
                unitPurchasePrice = dto.unitPurchasePrice ?: existingMovement.unitPurchasePrice,
                unitSalePrice = dto.unitSalePrice ?: existingMovement.unitSalePrice
            )
        )

        logger.info("Mensagem UPDATE enviada para Kafka com status PENDING: $id")

        return InventoryMovementResponse(
            movementId = id,
            status = "PENDING"
        )
    }

    override fun deleteMovement(id: String, userId: String): InventoryMovementResponse {
        logger.info("Processando DELETE para movimento: $id")

        val movement = inventoryMovementRepository.findById(id)
            .orElseThrow { NoSuchElementException("Movimento $id não encontrado") }

        if (movement.deleted) {
            logger.warn("Tentativa de deletar movimento já deletado: $id")
            throw IllegalArgumentException("Movimento $id já foi deletado")
        }

        if (movement.type == MovementType.ADJUST) {
            logger.error("Tentativa de deletar movimento de ajuste direto: $id")
            throw IllegalArgumentException("Não é permitido deletar movimentos de ajuste direto")
        }

        val inventory = inventoryRepository.findById(movement.product.id)
            .orElseThrow { NoSuchElementException("Estoque do produto ${movement.product.id} não encontrado") }

        if (movement.type == MovementType.IN && inventory.quantity < movement.quantity) {
            logger.error("Não é possível deletar movimento IN - estoque insuficiente: $id")
            throw IllegalArgumentException(
                "Não é possível deletar o movimento: estoque insuficiente para reverter a entrada " +
                        "(Estoque atual: ${inventory.quantity}, Quantidade do movimento: ${movement.quantity})"
            )
        }

        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("Usuário $userId não encontrado") }

        logger.info("Validação OK. Enviando mensagem DELETE para Kafka: $id")

        producer.sendMovementMessage(
            InventoryMovementMessage(
                movementId = id,
                productId = movement.product.id,
                userId = movement.user.id,
                quantity = movement.quantity,
                type = movement.type.toString(),
                operation = Operation.DELETE,
                description = "Deleção: ${movement.description}",
                createdBy = userId,
                unitPurchasePrice = movement.unitPurchasePrice,
                unitSalePrice = movement.unitSalePrice
            )
        )

        logger.info("Mensagem DELETE enviada para Kafka com status PENDING: $id")

        return InventoryMovementResponse(
            movementId = id,
            status = "PENDING"
        )
    }


    private fun InventoryMovement.toResponseDTO() = InventoryMovementResponseDTO(
        id = this.id,
        productId = this.product.id,
        userId = this.user.id,
        quantity = this.quantity,
        unitPurchasePrice = this.unitPurchasePrice,
        unitSalePrice = this.unitSalePrice,
        type = this.type.name,
        description = this.description,
        createdBy = this.createdBy,
        createdAt = this.createdAt,
        updatedBy = this.updatedBy,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}