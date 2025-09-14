package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.CardRequestDTO
import com.rtr.store_manager_api.dto.CardResponseDTO

interface CardService {
    fun createCard(dto: CardRequestDTO, userId: String): CardResponseDTO
    fun getAllCards(): List<CardResponseDTO>
    fun getCardById(id: String): CardResponseDTO
    fun updateCard(id: String, dto: CardRequestDTO, userId: String): CardResponseDTO
    fun deleteCard(id: String, userId: String)
}