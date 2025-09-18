package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.domain.entity.Inventory
import com.rtr.store_manager_api.dto.InventoryRequestDTO
import com.rtr.store_manager_api.dto.InventoryResponseDTO
import com.rtr.store_manager_api.repository.InventoryRepository
import com.rtr.store_manager_api.repository.ProductRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class InventoryServiceImpl(
    private val inventoryRepository: InventoryRepository,
    private val productRepository: ProductRepository
) : InventoryService {

    override fun createInventory(dto: InventoryRequestDTO, userId: String): InventoryResponseDTO {
        val product = productRepository.findById(dto.productId!!)
            .orElseThrow { NoSuchElementException("Produto ${dto.productId} não encontrado") }

        val inventory = Inventory(
            product = product,
            quantity = dto.quantity
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        return inventoryRepository.save(inventory).toResponseDTO()
    }

    override fun getAllInventory(): List<InventoryResponseDTO> =
        inventoryRepository.findAll().map { it.toResponseDTO() }

    override fun getInventoryByProductId(productId: String): InventoryResponseDTO =
        inventoryRepository.findById(productId)
            .orElseThrow { NoSuchElementException("Estoque do produto $productId não encontrado") }
            .toResponseDTO()

    override fun updateInventory(productId: String, dto: InventoryRequestDTO, userId: String): InventoryResponseDTO {
        val existing = inventoryRepository.findById(productId)
            .orElseThrow { NoSuchElementException("Estoque do produto $productId não encontrado") }

        dto.quantity.let { existing.quantity = it }

        existing.updatedBy = userId
        existing.updatedAt = LocalDateTime.now()

        return inventoryRepository.save(existing).toResponseDTO()
    }

    override fun deleteInventory(productId: String, userId: String) {
        val inventory = inventoryRepository.findById(productId)
            .orElseThrow { NoSuchElementException("Estoque do produto $productId não encontrado") }

        inventory.deleted = true
        inventory.updatedBy = userId
        inventory.updatedAt = LocalDateTime.now()

        inventoryRepository.save(inventory)
    }

    private fun Inventory.toResponseDTO() = InventoryResponseDTO(
        productId = this.product.id,
        productName = this.product.name,
        quantity = this.quantity,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}
