package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.Inventory
import com.rtr.store_manager_api.domain.entity.InventoryMovement
import com.rtr.store_manager_api.domain.enum.MovementType
import com.rtr.store_manager_api.dto.InventoryMovementCreatedResponse
import com.rtr.store_manager_api.dto.InventoryMovementRequestDTO
import com.rtr.store_manager_api.dto.InventoryMovementResponseDTO
import com.rtr.store_manager_api.dto.InventoryMovementUpdateDTO
import com.rtr.store_manager_api.event.InventoryMovementMessage
import com.rtr.store_manager_api.event.InventoryProducer
import com.rtr.store_manager_api.repository.InventoryMovementRepository
import com.rtr.store_manager_api.repository.InventoryRepository
import com.rtr.store_manager_api.repository.ProductRepository
import com.rtr.store_manager_api.repository.UserRepository
import com.rtr.store_manager_api.service.InventoryMovementService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class InventoryMovementServiceImpl(
    private val inventoryMovementRepository: InventoryMovementRepository,
    private val inventoryRepository: InventoryRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val producer: InventoryProducer
) : InventoryMovementService {

    override fun createMovement(dto: InventoryMovementRequestDTO, userId: String): InventoryMovementCreatedResponse   {
        val product = productRepository.findById(dto.productId)
            .orElseThrow { NoSuchElementException("Produto ${dto.productId} não encontrado") }

        val user = userRepository.findById(dto.userId)
            .orElseThrow { NoSuchElementException("Usuário ${dto.userId} não encontrado") }

        val movement = InventoryMovement(
            product = product,
            user = user,
            quantity = dto.quantity,
            unitPurchasePrice = dto.unitPurchasePrice,
            unitSalePrice = dto.unitSalePrice,
            type = MovementType.valueOf(dto.type.uppercase()),
            description = dto.description
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        producer.sendMovement(
            InventoryMovementMessage(
                movementId = movement.id,
                productId = product.id,
                userId = user.id,
                quantity = movement.quantity,
                type = movement.type.name,
                description = movement.description,
                createdBy = userId,
                unitPurchasePrice = movement.unitPurchasePrice,
                unitSalePrice = movement.unitSalePrice
            )
        )

        return InventoryMovementCreatedResponse(
            movementId = movement.id,
            status = "PENDING"
        )
    }

    override fun getAllMovements(): List<InventoryMovementResponseDTO> =
        inventoryMovementRepository.findAll().map { it.toResponseDTO() }

    override fun getMovementById(id: String): InventoryMovementResponseDTO =
        inventoryMovementRepository.findById(id)
            .orElseThrow { NoSuchElementException("Movimento $id não encontrado") }
            .toResponseDTO()

    override fun updateMovement(id: String, dto: InventoryMovementUpdateDTO, userId: String): InventoryMovementResponseDTO {

        val existingMovement = inventoryMovementRepository.findById(id)
            .orElseThrow { NoSuchElementException("Movimento $id não encontrado") }

        dto.unitPurchasePrice?.let { existingMovement.unitPurchasePrice = it }
        dto.unitSalePrice?.let { existingMovement.unitSalePrice = it }
        dto.description?.let { existingMovement.description = it }

        existingMovement.updatedBy = userId
        existingMovement.updatedAt = LocalDateTime.now()

        val savedMovement = inventoryMovementRepository.save(existingMovement)

        return savedMovement.toResponseDTO()
    }

    override fun deleteMovement(id: String, userId: String) {
        val movement = inventoryMovementRepository.findById(id)
            .orElseThrow { NoSuchElementException("Movimento $id não encontrado") }

        val inventory = inventoryRepository.findById(movement.product.id)
            .orElseThrow { NoSuchElementException("Estoque do produto ${movement.product.id} não encontrado") }

        when (movement.type) {
            MovementType.IN -> {
                if (inventory.quantity < movement.quantity) {
                    throw IllegalArgumentException("Não é possível deletar o movimento: estoque insuficiente para reverter a entrada")
                }
                inventory.quantity -= movement.quantity
            }
            MovementType.OUT -> inventory.quantity += movement.quantity
            MovementType.ADJUST -> throw IllegalArgumentException("Não é permitido deletar movimentos de ajuste direto")
        }

        inventory.updatedBy = userId
        inventory.updatedAt = LocalDateTime.now()
        inventoryRepository.save(inventory)

        movement.deleted = true
        movement.updatedBy = userId
        movement.updatedAt = LocalDateTime.now()
        inventoryMovementRepository.save(movement)
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
