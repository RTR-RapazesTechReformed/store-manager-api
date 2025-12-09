package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.Card
import com.rtr.store_manager_api.domain.entity.Inventory
import com.rtr.store_manager_api.domain.entity.OtherProduct
import com.rtr.store_manager_api.domain.entity.Product
import com.rtr.store_manager_api.domain.entity.Store
import com.rtr.store_manager_api.dto.CardResponseDTO
import com.rtr.store_manager_api.dto.OtherProductResponseDTO
import com.rtr.store_manager_api.dto.ProductRequestDTO
import com.rtr.store_manager_api.dto.ProductResponseDTO
import com.rtr.store_manager_api.dto.ProductUpdateDTO
import com.rtr.store_manager_api.dto.UserResponseDTO
import com.rtr.store_manager_api.repository.CardRepository
import com.rtr.store_manager_api.repository.InventoryRepository
import com.rtr.store_manager_api.repository.OtherProductRepository
import com.rtr.store_manager_api.repository.ProductRepository
import com.rtr.store_manager_api.repository.StoreRepository
import com.rtr.store_manager_api.service.ProductService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val cardRepository: CardRepository,
    private val storeRepository: StoreRepository,
    private val otherProductRepository: OtherProductRepository,
    private val inventoryRepository: InventoryRepository
) : ProductService {

    override fun createProduct(dto: ProductRequestDTO, userId: String): ProductResponseDTO {
        val card: Card? = dto.cardId?.let { cardRepository.findByIdAndDeletedFalse(it).orElse(null) }
        val store: Store = storeRepository.findByIdAndDeletedFalse(dto.storeId).orElseThrow {
            NoSuchElementException("Store ${dto.storeId} não encontrada")
        }

        val otherProduct: OtherProduct? = when {
            dto.otherProductId != null -> otherProductRepository.findByIdAndDeletedFalse(dto.otherProductId)
                .orElseThrow { NoSuchElementException("${dto.otherProductId} não encontrado") }

            dto.otherProduct != null -> {
                val otherDto = dto.otherProduct
                OtherProduct(
                    type = otherDto.type,
                    nationality = otherDto.nationality,
                    packageContents = otherDto.packageContents,
                    extraInfo = otherDto.extraInfo,
                ).apply {
                    createdBy = userId
                    updatedBy = userId
                }.also { otherProductRepository.save(it) }
            }

            else -> null
        }

        val product = Product(
            name = dto.name,
            description = dto.description,
            card = card,
            otherProduct = otherProduct,
            price = dto.price,
            condition = dto.condition,
            store = store
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        val savedProduct = productRepository.save(product)

        val inventory = Inventory(
            product = savedProduct,
            quantity = 0
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        inventoryRepository.save(inventory)

        return productRepository.save(product).toResponseDTO()
    }



    override fun getAllProducts(storeId: String?,): List<ProductResponseDTO> {
        val products = productRepository.findAllByDeletedFalse()
        return products.filter { product ->
            (storeId == null || product.store?.id == storeId)
        }.map { it.toResponseDTO() }
    }

    override fun getProductById(id: String): ProductResponseDTO =
        productRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { NoSuchElementException("Produto $id não encontrado") }
            .toResponseDTO()

    override fun updateProduct(id: String, dto: ProductUpdateDTO, userId: String): ProductResponseDTO {
        val existing = productRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { NoSuchElementException("Produto $id não encontrado") }

        dto.name?.let { existing.name = it }
        dto.description?.let { existing.description = it }
        dto.storeId?.let {
            val store = storeRepository.findByIdAndDeletedFalse(it)
                .orElseThrow { NoSuchElementException("Loja $it não encontrada") }
            existing.store = store
        }
        dto.price?.let { existing.price = it }
        dto.condition?.let { existing.condition = it }

        existing.updatedAt = LocalDateTime.now()
        existing.updatedBy = userId

        return productRepository.save(existing).toResponseDTO()
    }

    override fun deleteProduct(id: String, userId: String) {
        val product = productRepository.findByIdAndDeletedFalse(id)
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
        price = this.price,
        condition = this.condition,
        storeName = this.store?.name,
        card = this.card?.let {
            CardResponseDTO(
                id = it.id,
                title = it.title,
                season = it.season,
                pokemonType = it.pokemonType.toString(),
                collectionId = it.collection?.id ?: "",
                code = it.code,
                rarity = it.rarity.toString(),
                nationality = it.nationality,
                createdBy = it.createdBy,
                createdAt = it.createdAt,
                updatedBy = it.updatedBy,
                updatedAt = it.updatedAt,
                deleted = it.deleted
            )
        },
        otherProduct = this.otherProduct?.let {
            OtherProductResponseDTO(
                id = it.id,
                type = it.type,
                nationality = it.nationality,
                packageContents = it.packageContents,
                extraInfo = it.extraInfo,
                createdBy = it.createdBy,
                updatedBy = it.updatedBy,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                deleted = it.deleted
            )
        },
        createdBy = this.createdBy,
        updatedBy = this.updatedBy,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}
