package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.CollectionRequestDTO
import com.rtr.store_manager_api.dto.CollectionResponseDTO
import com.rtr.store_manager_api.service.CollectionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/collections")
class CollectionController(
    private val collectionService: CollectionService
) {

    @PostMapping
    fun createCollection(
        @RequestBody dto: CollectionRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<CollectionResponseDTO> {
        val created = collectionService.createCollection(dto, userId)
        return  ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAllCollections(): ResponseEntity<List<CollectionResponseDTO>> =
        ResponseEntity.ok(collectionService.getAllCollections())

    @GetMapping("/{id}")
    fun getCollectionById(@PathVariable id: String): ResponseEntity<CollectionResponseDTO> =
        ResponseEntity.ok(collectionService.getCollectionById(id))

    @PutMapping("/{id}")
    fun updateCollection(
        @PathVariable id: String,
        @RequestBody dto: CollectionRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<CollectionResponseDTO> =
        ResponseEntity.ok(collectionService.updateCollection(id, dto, userId))

    @DeleteMapping("/{id}")
    fun deleteCollection(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        collectionService.deleteCollection(id, userId)
        return ResponseEntity.noContent().build()
    }
}
