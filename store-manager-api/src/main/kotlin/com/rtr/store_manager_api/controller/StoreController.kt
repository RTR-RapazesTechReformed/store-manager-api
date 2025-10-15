package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.StoreRequestDTO
import com.rtr.store_manager_api.dto.StoreResponseDTO
import com.rtr.store_manager_api.dto.StoreUpdateDTO
import com.rtr.store_manager_api.service.StoreService
import com.rtr.store_manager_api.util.HeaderValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/stores")
class StoreController(
    private val storeService: StoreService
) {

    @PostMapping
    fun create(
        @RequestBody storeInput: StoreRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<StoreResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val createdStore = storeService.createStore(storeInput, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<StoreResponseDTO>> =
        ResponseEntity.ok(storeService.getAllStores())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<StoreResponseDTO> {
        val store = storeService.getStoreById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(store)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody storeUpdate: StoreUpdateDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<StoreResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val updatedStore = storeService.updateStore(id, storeUpdate, userId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updatedStore)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        HeaderValidator.validateUserId(userId)
        val deleted = storeService.deleteStore(id, userId)
        return if (deleted) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}
