package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.Card
import com.rtr.store_manager_api.domain.entity.Product
import com.rtr.store_manager_api.dto.ProductRequestDTO
import com.rtr.store_manager_api.dto.ProductResponseDTO
import com.rtr.store_manager_api.dto.ProductUpdateDTO
import com.rtr.store_manager_api.repository.CardRepository
import com.rtr.store_manager_api.repository.ProductRepository
import com.rtr.store_manager_api.service.ProductService
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

    override fun updateProduct(id: String, dto: ProductUpdateDTO, userId: String): ProductResponseDTO {
        val existing = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Produto $id não encontrado") }

        dto.name?.let { existing.name = it }
        dto.description?.let { existing.description = it }

        dto.price?.let { existing.price = it }
        dto.condition?.let { existing.condition = it }

        existing.updatedAt = LocalDateTime.now()
        existing.updatedBy = userId

        return productRepository.save(existing).toResponseDTO()
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
        cardId = this.card?.id.toString(),
        otherProductId = this.otherProduct?.id.toString(),
        price = this.price,
        condition = this.condition,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}
