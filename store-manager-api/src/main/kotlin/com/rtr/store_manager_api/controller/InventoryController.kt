package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.InventoryRequestDTO
import com.rtr.store_manager_api.dto.InventoryResponseDTO
import com.rtr.store_manager_api.service.InventoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/inventory")
class InventoryController(
    private val inventoryService: InventoryService
) {

    @PostMapping
    fun create(
        @RequestBody dto: InventoryRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<InventoryResponseDTO> {
        val created = inventoryService.createInventory(dto, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<InventoryResponseDTO>> =
        ResponseEntity.ok(inventoryService.getAllInventory())

    @GetMapping("/{productId}")
    fun getById(@PathVariable productId: String): ResponseEntity<InventoryResponseDTO> =
        ResponseEntity.ok(inventoryService.getInventoryByProductId(productId))

    @PutMapping("/{productId}")
    fun update(
        @PathVariable productId: String,
        @RequestBody dto: InventoryRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<InventoryResponseDTO> =
        ResponseEntity.ok(inventoryService.updateInventory(productId, dto, userId))

    @DeleteMapping("/{productId}")
    fun delete(
        @PathVariable productId: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        inventoryService.deleteInventory(productId, userId)
        return ResponseEntity.noContent().build()
    }
}
