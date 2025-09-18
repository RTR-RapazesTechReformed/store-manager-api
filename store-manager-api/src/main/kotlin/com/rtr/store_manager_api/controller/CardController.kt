package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.CardRequestDTO
import com.rtr.store_manager_api.dto.CardResponseDTO
import com.rtr.store_manager_api.service.CardService
import com.rtr.store_manager_api.util.HeaderValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/cards")
class CardController(
    private val cardService: CardService
) {

    @PostMapping
    fun createCard(
        @RequestBody dto: CardRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<CardResponseDTO> {
        val created = cardService.createCard(dto, userId)
        HeaderValidator.validateUserId(userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAllCards(): ResponseEntity<List<CardResponseDTO>> {
        val list = cardService.getAllCards()
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getCardById(@PathVariable id: String): ResponseEntity<CardResponseDTO> {
        val dto = cardService.getCardById(id)
        return ResponseEntity.ok(dto)
    }

    @PutMapping("/{id}")
    fun updateCard(
        @PathVariable id: String,
        @RequestBody dto: CardRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<CardResponseDTO> {
        val updated = cardService.updateCard(id, dto, userId)
        HeaderValidator.validateUserId(userId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteCard(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        cardService.deleteCard(id, userId)
        HeaderValidator.validateUserId(userId)
        return ResponseEntity.noContent().build()
    }
}
