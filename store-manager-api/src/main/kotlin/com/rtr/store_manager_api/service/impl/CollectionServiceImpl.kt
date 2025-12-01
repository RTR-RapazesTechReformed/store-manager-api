package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.Collection
import com.rtr.store_manager_api.dto.CardSummaryDTO
import com.rtr.store_manager_api.dto.CollectionRequestDTO
import com.rtr.store_manager_api.dto.CollectionResponseDTO
import com.rtr.store_manager_api.dto.CollectionWithCardsDTO
import com.rtr.store_manager_api.repository.CardRepository
import com.rtr.store_manager_api.repository.CollectionRepository
import com.rtr.store_manager_api.service.CollectionService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.NoSuchElementException

@Service
class CollectionServiceImpl(
    private val collectionRepository: CollectionRepository,
    private val cardRepository: CardRepository
) : CollectionService {

    override fun createCollection(dto: CollectionRequestDTO, userId: String): CollectionResponseDTO {
        val collection = Collection(
            name = dto.name,
            abbreviation = dto.abbreviation,
            releaseDate = dto.releaseDate
        ).apply {
            createdBy = userId
            updatedBy = userId
        }
        return collectionRepository.save(collection).toResponseDTO()
    }

    override fun getAllCollections(): List<CollectionResponseDTO> =
        collectionRepository.findAllByDeletedFalse().map { it.toResponseDTO() }

    override fun getCollectionById(id: String): CollectionResponseDTO {
        val collection = collectionRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { NoSuchElementException("Coleção $id não encontrada") }
        if (collection.deleted) throw IllegalArgumentException("Coleção $id foi marcada como deletada")
        return collection.toResponseDTO()
    }

    override fun updateCollection(id: String, dto: CollectionRequestDTO, userId: String): CollectionResponseDTO {
        val existing = collectionRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { NoSuchElementException("Coleção $id não encontrada") }

        val updated = existing.copy(
            name = dto.name,
            abbreviation = dto.abbreviation,
            releaseDate = dto.releaseDate
        ).apply {
            createdBy = existing.createdBy
            createdAt = existing.createdAt
            updatedBy = userId
            updatedAt = LocalDateTime.now()
        }

        return collectionRepository.save(updated).toResponseDTO()
    }

    override fun getCollectionsWithCards(
        rarity: String?,
        pokemonType: String?,
        nationality: String?
    ): List<CollectionWithCardsDTO> {
        val collections = collectionRepository.findAllByDeletedFalse().filter { !it.deleted }

        return collections.map { col ->
            val cards = cardRepository.findByCollectionWithFilters(
                collectionId = col.id,
                rarity = rarity,
                pokemonType = pokemonType,
                nationality = nationality
            ).map { card ->
                CardSummaryDTO(
                    id = card.id,
                    title = card.title,
                    pokemonType = card.pokemonType?.name,
                    code = card.code,
                    rarity = card.rarity.name,
                    nationality = card.nationality
                )
            }

            CollectionWithCardsDTO(
                id = col.id,
                name = col.name,
                abbreviation = col.abbreviation,
                releaseDate = col.releaseDate?.toString(),
                generation = col.generation,
                cards = cards
            )
        }
    }

    override fun deleteCollection(id: String, userId: String) {
        val collection = collectionRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { NoSuchElementException("Coleção $id não encontrada") }

        collection.deleted = true
        collection.updatedBy = userId
        collection.updatedAt = LocalDateTime.now()

        collectionRepository.save(collection)
    }

    private fun Collection.toResponseDTO() = CollectionResponseDTO(
        id = this.id,
        name = this.name,
        abbreviation = this.abbreviation,
        releaseDate = this.releaseDate,
        createdBy = this.createdBy,
        createdAt = this.createdAt,
        updatedBy = this.updatedBy,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}
