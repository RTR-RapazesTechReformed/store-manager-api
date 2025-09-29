package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.CollectionRequestDTO
import com.rtr.store_manager_api.dto.CollectionResponseDTO
import com.rtr.store_manager_api.dto.CollectionWithCardsDTO

interface CollectionService {
    fun createCollection(dto: CollectionRequestDTO, userId: String): CollectionResponseDTO
    fun getAllCollections(): List<CollectionResponseDTO>
    fun getCollectionsWithCards(
        rarity: String?,
        pokemonType: String?,
        nationality: String?
    ): List<CollectionWithCardsDTO>
    fun getCollectionById(id: String): CollectionResponseDTO
    fun updateCollection(id: String, dto: CollectionRequestDTO, userId: String): CollectionResponseDTO
    fun deleteCollection(id: String, userId: String)
}