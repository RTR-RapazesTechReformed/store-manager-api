package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.Collection
import com.rtr.store_manager_api.dto.CollectionRequestDTO
import com.rtr.store_manager_api.dto.CollectionResponseDTO
import com.rtr.store_manager_api.repository.CollectionRepository
import com.rtr.store_manager_api.service.CollectionService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.NoSuchElementException

@Service
class CollectionServiceImpl(
    private val collectionRepository: CollectionRepository
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
        collectionRepository.findAll().filter { !it.deleted }.map { it.toResponseDTO() }

    override fun getCollectionById(id: String): CollectionResponseDTO {
        val collection = collectionRepository.findById(id)
            .orElseThrow { NoSuchElementException("Coleção $id não encontrada") }
        if (collection.deleted) throw IllegalArgumentException("Coleção $id foi marcada como deletada")
        return collection.toResponseDTO()
    }

    override fun updateCollection(id: String, dto: CollectionRequestDTO, userId: String): CollectionResponseDTO {
        val existing = collectionRepository.findById(id)
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

    override fun deleteCollection(id: String, userId: String) {
        val collection = collectionRepository.findById(id)
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
