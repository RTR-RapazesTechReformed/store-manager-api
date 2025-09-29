package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.domain.enum.CardRarity
import com.rtr.store_manager_api.domain.enum.PokemonType
import com.rtr.store_manager_api.dto.CardRequestDTO
import com.rtr.store_manager_api.dto.CardResponseDTO

interface CardService {
    fun createCard(dto: CardRequestDTO, userId: String): CardResponseDTO
    fun getAllCards(
        collectionId: String?,
        pokemonType: PokemonType?,
        title: String?,
        rarity: CardRarity?,
        nationality: String?): List<CardResponseDTO>
    fun getCardById(id: String): CardResponseDTO
    fun updateCard(id: String, dto: CardRequestDTO, userId: String): CardResponseDTO
    fun deleteCard(id: String, userId: String)
}