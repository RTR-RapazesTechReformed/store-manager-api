package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.domain.entity.Inventory
import com.rtr.store_manager_api.domain.entity.InventoryMovement
import com.rtr.store_manager_api.domain.enum.MovementType
import com.rtr.store_manager_api.dto.InventoryMovementRequestDTO
import com.rtr.store_manager_api.dto.InventoryMovementResponseDTO
import com.rtr.store_manager_api.dto.InventoryMovementUpdateDTO
import com.rtr.store_manager_api.repository.InventoryMovementRepository
import com.rtr.store_manager_api.repository.InventoryRepository
import com.rtr.store_manager_api.repository.ProductRepository
import com.rtr.store_manager_api.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class InventoryMovementServiceImpl(
    private val inventoryMovementRepository: InventoryMovementRepository,
    private val inventoryRepository: InventoryRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : InventoryMovementService {

    override fun createMovement(dto: InventoryMovementRequestDTO, userId: String): InventoryMovementResponseDTO {
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

        val savedMovement = inventoryMovementRepository.save(movement)

        val inventory = inventoryRepository.findById(product.id).orElseGet {
            Inventory(
                product = product,
                quantity = if (movement.type == MovementType.IN || movement.type == MovementType.ADJUST)
                    movement.quantity else 0
            ).apply {
                createdBy = userId
                updatedBy = userId
            }
        }

        if (inventoryRepository.existsById(product.id)) {
            when (movement.type) {
                MovementType.IN -> inventory.quantity += movement.quantity
                MovementType.OUT -> {
                    if (inventory.quantity < movement.quantity) {
                        throw IllegalArgumentException("Estoque insuficiente para saída")
                    }
                    inventory.quantity -= movement.quantity
                }
                MovementType.ADJUST -> inventory.quantity = movement.quantity
            }
        }

        inventory.updatedBy = userId
        inventory.updatedAt = LocalDateTime.now()
        inventoryRepository.save(inventory)


        inventory.updatedBy = userId
        inventory.updatedAt = LocalDateTime.now()
        inventoryRepository.save(inventory)

        return savedMovement.toResponseDTO()
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
