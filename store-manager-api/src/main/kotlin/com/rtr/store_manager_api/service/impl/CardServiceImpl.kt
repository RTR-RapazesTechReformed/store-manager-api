package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.Card
import com.rtr.store_manager_api.domain.enum.CardRarity
import com.rtr.store_manager_api.dto.CardRequestDTO
import com.rtr.store_manager_api.dto.CardResponseDTO
import com.rtr.store_manager_api.repository.CardRepository
import com.rtr.store_manager_api.repository.CollectionRepository
import com.rtr.store_manager_api.service.CardService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CardServiceImpl(
    private val cardRepository: CardRepository,
    private val collectionRepository: CollectionRepository
) : CardService {

    override fun createCard(dto: CardRequestDTO, userId: String): CardResponseDTO {
        val collection = collectionRepository.findById(dto.collectionId)
            .orElseThrow { IllegalArgumentException("Coleção não encontrada") }

        val card = Card(
            title = dto.title,
            artistName = dto.artistName,
            season = dto.season,
            collection = collection,
            code = dto.code,
            rarity = CardRarity.valueOf(dto.rarity.uppercase())
        )
        card.createdBy = userId
        card.updatedBy = userId

        val saved = cardRepository.save(card)
        return saved.toResponseDTO()
    }

    override fun getAllCards(): List<CardResponseDTO> =
        cardRepository.findAll().filter { !it.deleted }.map { it.toResponseDTO() }

    override fun getCardById(id: String): CardResponseDTO {
        val card = cardRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Carta não encontrada") }
        if (card.deleted) throw IllegalArgumentException("Esta carta foi excluída")
        return card.toResponseDTO()
    }

    override fun updateCard(id: String, dto: CardRequestDTO, userId: String): CardResponseDTO {
        val existing = cardRepository.findById(id)
            .orElseThrow { NoSuchElementException("Carta $id não encontrada") }

        val collection = collectionRepository.findById(dto.collectionId)
            .orElseThrow { NoSuchElementException("Coleção ${dto.collectionId} não encontrada") }

        val updated = existing.copy(
            title = dto.title,
            artistName = dto.artistName,
            season = dto.season,
            collection = collection,
            code = dto.code,
            rarity = CardRarity.valueOf(dto.rarity.uppercase())
        ).apply {
            createdBy = existing.createdBy
            createdAt = existing.createdAt
            updatedBy = userId
            updatedAt = LocalDateTime.now()
        }

        return cardRepository.save(updated).toResponseDTO()
    }

    override fun deleteCard(id: String, userId: String) {
        val card = cardRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Carta não encontrada") }
        card.deleted = true
        card.updatedBy = userId
        card.updatedAt = LocalDateTime.now()
        cardRepository.save(card)
    }

    private fun Card.toResponseDTO() = CardResponseDTO(
        id = this.id,
        title = this.title,
        artistName = this.artistName,
        season = this.season,
        collectionId = this.collection.id,
        code = this.code,
        rarity = this.rarity.name,
        createdBy = this.createdBy,
        createdAt = this.createdAt,
        updatedBy = this.updatedBy,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}
