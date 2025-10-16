package com.rtr.store_manager_api.event

import com.rtr.store_manager_api.domain.entity.Inventory
import com.rtr.store_manager_api.domain.entity.InventoryAudit
import com.rtr.store_manager_api.domain.entity.InventoryMovement
import com.rtr.store_manager_api.domain.enum.MovementType
import com.rtr.store_manager_api.repository.InventoryAuditRepository
import com.rtr.store_manager_api.repository.InventoryMovementRepository
import com.rtr.store_manager_api.repository.InventoryRepository
import com.rtr.store_manager_api.repository.ProductRepository
import com.rtr.store_manager_api.repository.UserRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class InventoryConsumer(
    private val inventoryMovementRepository: InventoryMovementRepository,
    private val inventoryRepository: InventoryRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val inventoryAuditRepository: InventoryAuditRepository,
    private val producer: InventoryProducer
) {

    @KafkaListener(topics = ["inventory-movement-topic"], groupId = "store-manager-group")
    fun consumeMovement(message: InventoryMovementMessage) {
        try {
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
                MovementType.IN -> inventory.quantity + message.quantity
                MovementType.OUT -> {
                    if (inventory.quantity < message.quantity) {
                        producer.sendToDLQ(
                            InventoryMovementErrorMessage(
                                movementId = message.movementId,
                                productId = product.id,
                                userId = user.id,
                                quantity = message.quantity,
                                type = message.type,
                                errorMessage = "Estoque insuficiente para saída"
                            )
                        )
                        return
                    }
                    inventory.quantity - message.quantity
                }
                MovementType.ADJUST -> message.quantity
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
                description = message.description
            ).apply {
                createdBy = message.createdBy
                updatedBy = message.createdBy
            }
            val savedMovement = inventoryMovementRepository.save(movement)

            inventoryAuditRepository.save(
                InventoryAudit(
                    product = product,
                    movement = savedMovement,
                    quantity = message.quantity,
                    quantityBefore = quantityBefore,
                    quantityAfter = quantityAfter,
                    userId = user.id,
                    movementType = movement.type.name,
                    description = movement.description
                )
            )

        } catch (ex: Exception) {
            producer.sendToDLQ(
                InventoryMovementErrorMessage(
                    movementId = message.movementId,
                    productId = message.productId,
                    userId = message.userId,
                    quantity = message.quantity,
                    type = message.type,
                    errorMessage = ex.message ?: "Erro desconhecido"
                )
            )
        }
    }
}