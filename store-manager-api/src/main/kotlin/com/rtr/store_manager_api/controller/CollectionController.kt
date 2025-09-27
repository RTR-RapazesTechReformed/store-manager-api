package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.CollectionRequestDTO
import com.rtr.store_manager_api.dto.CollectionResponseDTO
import com.rtr.store_manager_api.service.CollectionService
import com.rtr.store_manager_api.util.HeaderValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/collections")
class CollectionController(
    private val collectionService: CollectionService
) {

    @PostMapping // TODO: FUNCIONA
    fun createCollection(
        @RequestBody dto: CollectionRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<CollectionResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val created = collectionService.createCollection(dto, userId)
        return  ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAllCollections(): ResponseEntity<List<CollectionResponseDTO>> {

        val collections = collectionService.getAllCollections()

        return if (collections.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(collections)
        }

    }

    @GetMapping("/{id}")
    fun getCollectionById(@PathVariable id: String): ResponseEntity<CollectionResponseDTO> =
        ResponseEntity.ok(collectionService.getCollectionById(id))

    @PutMapping("/{id}")
    fun updateCollection(
        @PathVariable id: String,
        @RequestBody dto: CollectionRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<CollectionResponseDTO> {
        HeaderValidator.validateUserId(userId)
        var updated = collectionService.updateCollection(id, dto, userId)
        return ResponseEntity.status(HttpStatus.OK).body(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteCollection(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        HeaderValidator.validateUserId(userId)
        collectionService.deleteCollection(id, userId)
        return ResponseEntity.noContent().build()
    }
}
