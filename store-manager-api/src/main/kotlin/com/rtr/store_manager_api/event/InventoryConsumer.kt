package com.rtr.store_manager_api.event

import com.rtr.store_manager_api.domain.entity.Inventory
import com.rtr.store_manager_api.domain.entity.InventoryMovement
import com.rtr.store_manager_api.domain.enum.AuditStatus
import com.rtr.store_manager_api.domain.enum.MovementType
import com.rtr.store_manager_api.domain.enum.Operation
import com.rtr.store_manager_api.repository.InventoryAuditRepository
import com.rtr.store_manager_api.repository.InventoryMovementRepository
import com.rtr.store_manager_api.repository.InventoryRepository
import com.rtr.store_manager_api.repository.ProductRepository
import com.rtr.store_manager_api.repository.UserRepository
import com.rtr.store_manager_api.service.AuditService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class InventoryConsumer(
    private val inventoryMovementRepository: InventoryMovementRepository,
    private val inventoryRepository: InventoryRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val auditService: AuditService,
    private val producer: InventoryProducer
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["inventory-movement-topic"], groupId = "store-manager-group")
    fun consumeMovement(message: InventoryMovementMessage) {
        logger.info("Consumindo mensagem: ${message.movementId} - Operação: ${message.operation} - Tipo: ${message.type}")

        try {
            when (message.operation) {
                Operation.UPDATE -> processUpdate(message)
                Operation.DELETE -> processDelete(message)
                Operation.CREATE -> processCreate(message)
                else -> throw IllegalArgumentException("Operação inválida: ${message.operation}")
            }

            logger.info("Movimento processado com sucesso: ${message.movementId}")

        } catch (ex: NoSuchElementException) {
            logger.error("Erro de validação: ${ex.message}", ex)
            val errorMessage = InventoryMovementErrorMessage(
                movementId = message.movementId,
                productId = message.productId,
                userId = message.userId,
                quantity = message.quantity,
                type = message.type,
                operation = message.operation,
                errorMessage = ex.message ?: "Erro de validação"
            )
            producer.sendToDLQ(errorMessage)
        } catch (ex: Exception) {
            logger.error("Erro ao processar movimento: ${ex.message}", ex)
            val errorMessage = InventoryMovementErrorMessage(
                movementId = message.movementId,
                productId = message.productId,
                userId = message.userId,
                quantity = message.quantity,
                type = message.type,
                operation = message.operation,
                errorMessage = ex.message ?: "Erro desconhecido"
            )
            producer.sendToDLQ(errorMessage)
        }
    }

    private fun processCreate(message: InventoryMovementMessage) {
        logger.info("Processando CREATE para movimento: ${message.movementId}")

        val product = productRepository.findById(message.productId)
            .orElseThrow { NoSuchElementException("Produto ${message.productId} não encontrado") }

        val user = userRepository.findById(message.userId)
            .orElseThrow { NoSuchElementException("Usuário ${message.userId} não encontrado") }

        val inventory = inventoryRepository.findById(product.id).orElseGet {
            Inventory(product = product, quantity = 0).apply {
                createdBy = message.createdBy
                updatedBy = message.createdBy
            }
        }

        val quantityBefore = inventory.quantity
        val movementType = MovementType.valueOf(message.type.uppercase())

        val quantityAfter = when (movementType) {
            MovementType.IN -> {
                logger.info("Entrada de estoque: Produto ${product.id}, Quantidade: ${message.quantity}")
                inventory.quantity + message.quantity
            }
            MovementType.OUT -> {
                if (inventory.quantity < message.quantity) {
                    logger.error("Estoque insuficiente para saída: Produto ${product.id}")

                    val errorMessage = InventoryMovementErrorMessage(
                        movementId = message.movementId,
                        productId = product.id,
                        userId = user.id,
                        quantity = message.quantity,
                        type = message.type,
                        operation = message.operation,
                        errorMessage = "Estoque insuficiente para saída"
                    )
                    producer.sendToDLQ(errorMessage)
                    return
                }
                logger.info("Saída de estoque: Produto ${product.id}, Quantidade: ${message.quantity}")
                inventory.quantity - message.quantity
            }
            MovementType.ADJUST -> {
                logger.info("Ajuste de estoque: Produto ${product.id}, Quantidade: ${message.quantity}")
                message.quantity
            }
        }

        inventory.quantity = quantityAfter
        inventory.updatedAt = LocalDateTime.now()
        inventory.updatedBy = message.createdBy
        inventoryRepository.save(inventory)

        val movement = InventoryMovement(
            id = message.movementId,
            product = product,
            user = user,
            quantity = message.quantity,
            type = movementType,
            description = message.description,
            unitPurchasePrice = message.unitPurchasePrice,
            unitSalePrice = message.unitSalePrice
        ).apply {
            createdBy = message.createdBy
            updatedBy = message.createdBy
        }
        val savedMovement = inventoryMovementRepository.save(movement)

        auditService.logMovement(
            product = product,
            movement = savedMovement,
            userId = user.id,
            quantityBefore = quantityBefore,
            quantityAfter = quantityAfter,
            status = AuditStatus.PROCESSED,
            operation = message.operation,
            description = message.description
        )

        logger.info("CREATE processado com sucesso: ${message.movementId}")
    }

    private fun processUpdate(message: InventoryMovementMessage) {
        logger.info("Processando UPDATE para movimento: ${message.movementId}")

        val movement = inventoryMovementRepository.findById(message.movementId)
            .orElseThrow { NoSuchElementException("Movimento ${message.movementId} não encontrado") }

        val product = productRepository.findById(message.productId)
            .orElseThrow { NoSuchElementException("Produto ${message.productId} não encontrado") }

        val user = userRepository.findById(message.userId)
            .orElseThrow { NoSuchElementException("Usuário ${message.userId} não encontrado") }

        movement.unitPurchasePrice = message.unitPurchasePrice
        movement.unitSalePrice = message.unitSalePrice
        movement.description = message.description
        movement.updatedBy = message.createdBy
        movement.updatedAt = LocalDateTime.now()

        val savedMovement = inventoryMovementRepository.save(movement)

        logger.info("Movimento atualizado: ${message.movementId}")

        auditService.logMovement(
            product = product,
            movement = savedMovement,
            userId = user.id,
            quantityBefore = savedMovement.quantity,
            quantityAfter = savedMovement.quantity,
            status = AuditStatus.PROCESSED,
            operation = message.operation,
            description = "Atualização de dados"
        )

        logger.info("UPDATE processado com sucesso: ${message.movementId}")
    }


    private fun processDelete(message: InventoryMovementMessage) {
        logger.info("Processando DELETE para movimento: ${message.movementId}")

        val movement = inventoryMovementRepository.findById(message.movementId)
            .orElseThrow { NoSuchElementException("Movimento ${message.movementId} não encontrado") }

        val product = productRepository.findById(message.productId)
            .orElseThrow { NoSuchElementException("Produto ${message.productId} não encontrado") }

        val user = userRepository.findById(message.userId)
            .orElseThrow { NoSuchElementException("Usuário ${message.userId} não encontrado") }

        val inventory = inventoryRepository.findById(product.id)
            .orElseThrow { NoSuchElementException("Estoque do produto ${product.id} não encontrado") }

        val quantityBefore = inventory.quantity

        val quantityAfter = when (movement.type) {
            MovementType.IN -> {
                if (inventory.quantity < movement.quantity) {
                    logger.error("Não é possível reverter entrada - estoque insuficiente")
                    val errorMessage = InventoryMovementErrorMessage(
                        movementId = message.movementId,
                        productId = product.id,
                        userId = user.id,
                        quantity = movement.quantity,
                        type = movement.type.name,
                        operation = message.operation,
                        errorMessage = "Não é possível reverter entrada - estoque insuficiente"
                    )
                    producer.sendToDLQ(errorMessage)
                    return
                }
                inventory.quantity - movement.quantity
            }
            MovementType.OUT -> {
                inventory.quantity + movement.quantity
            }
            MovementType.ADJUST -> {
                val errorMessage = InventoryMovementErrorMessage(
                    movementId = message.movementId,
                    productId = product.id,
                    userId = user.id,
                    quantity = movement.quantity,
                    type = movement.type.name,
                    operation = message.operation,
                    errorMessage = "Não é permitido deletar movimentos de ajuste direto"
                )
                producer.sendToDLQ(errorMessage)
                return
            }
        }

        inventory.quantity = quantityAfter
        inventory.updatedAt = LocalDateTime.now()
        inventory.updatedBy = message.createdBy
        inventoryRepository.save(inventory)

        movement.deleted = true
        movement.updatedBy = message.createdBy
        movement.updatedAt = LocalDateTime.now()
        inventoryMovementRepository.save(movement)

        auditService.logMovement(
            product = product,
            movement = movement,
            userId = user.id,
            quantityBefore = quantityBefore,
            quantityAfter = quantityAfter,
            status = AuditStatus.PROCESSED,
            operation = message.operation,
            description = "Deleção de movimento"
        )

        logger.info("DELETE processado com sucesso: ${message.movementId}")
    }

    @KafkaListener(topics = ["inventory-movement-dlq"], groupId = "dlq-logger-group")
    fun processDLQ(message: InventoryMovementErrorMessage) {
        logger.error(
            """
            ╔════════════════════════════════════════════════════╗
            ║            MENSAGEM NA DLQ - ERRO CRÍTICO          ║
            ╠════════════════════════════════════════════════════╣
            ║ ID do Movimento: ${message.movementId}             ║
            ║ Produto ID: ${message.productId}                   ║
            ║ Usuário ID: ${message.userId}                      ║
            ║ Quantidade: ${message.quantity}                    ║
            ║ Tipo: ${message.type}                              ║
            ║ Erro: ${message.errorMessage}                      ║
            ║ Timestamp: ${message.timestamp}                    ║
            ╚════════════════════════════════════════════════════╝
            """.trimIndent()
        )

        try {
            val product = productRepository.findById(message.productId).orElse(null)
            val user = userRepository.findById(message.userId).orElse(null)

            if (product != null && user != null) {

                val errorMovement = InventoryMovement(
                    id = message.movementId,
                    product = product,
                    user = user,
                    quantity = message.quantity,
                    type = MovementType.valueOf(message.type.uppercase()),
                    description = "Erro: ${message.errorMessage}"
                ).apply {
                    createdBy = "SYSTEM"
                    updatedBy = "SYSTEM"
                }

                val savedMovement = inventoryMovementRepository.save(errorMovement)

                auditService.logMovement(
                    product = product,
                    movement = savedMovement,
                    userId = user.id,
                    quantityBefore = 0,
                    quantityAfter = 0,
                    status = AuditStatus.FAILED,
                    operation = message.operation,
                    errorMessage = message.errorMessage
                )

                logger.info("Erro registrado na auditoria: ${message.movementId}")
            } else {
                logger.error("Não foi possível registrar erro na auditoria - Produto ou Usuário não encontrado")
            }
        } catch (ex: Exception) {
            logger.error("Erro ao registrar falha na auditoria: ${ex.message}", ex)
        }
    }
}