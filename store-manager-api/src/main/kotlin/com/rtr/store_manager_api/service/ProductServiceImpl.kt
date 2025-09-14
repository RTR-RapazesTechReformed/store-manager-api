package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.domain.entity.Card
import com.rtr.store_manager_api.domain.entity.Product
import com.rtr.store_manager_api.dto.ProductRequestDTO
import com.rtr.store_manager_api.dto.ProductResponseDTO
import com.rtr.store_manager_api.repository.CardRepository
import com.rtr.store_manager_api.repository.ProductRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val cardRepository: CardRepository
) : ProductService {

    override fun createProduct(dto: ProductRequestDTO, userId: String): ProductResponseDTO {
        val card: Card? = dto.cardId?.let { cardRepository.findById(it).orElse(null) }

        val product = Product(
            name = dto.name,
            description = dto.description,
            type = dto.type,
            card = card,
            price = dto.price,
            condition = dto.condition
        ) .apply {
            createdBy = userId
            updatedBy = userId
        }

        return productRepository.save(product).toResponseDTO()
    }

    override fun getAllProducts(): List<ProductResponseDTO> =
        productRepository.findAll().map { it.toResponseDTO() }

    override fun getProductById(id: String): ProductResponseDTO =
        productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Produto $id não encontrado") }
            .toResponseDTO()

    override fun updateProduct(id: String, dto: ProductRequestDTO, userId: String): ProductResponseDTO {
        val existing = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Produto $id não encontrado") }

        val card: Card? = dto.cardId?.let { cardRepository.findById(it).orElse(null) }

        val updated = existing.copy(
            name = dto.name,
            description = dto.description,
            type = dto.type,
            card = card,
            price = dto.price,
            condition = dto.condition,
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        return productRepository.save(updated).toResponseDTO()
    }

    override fun deleteProduct(id: String, userId: String) {
        val product = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Produto $id não encontrado") }

        product.deleted = true
        product.updatedBy = userId
        product.updatedAt = LocalDateTime.now()

        productRepository.save(product)
    }

    private fun Product.toResponseDTO() = ProductResponseDTO(
        id = this.id,
        name = this.name,
        description = this.description,
        type = this.type,
        cardId = this.card?.id.toString(),
        price = this.price,
        condition = this.condition,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}
