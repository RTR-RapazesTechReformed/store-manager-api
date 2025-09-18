package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.InventoryRequestDTO
import com.rtr.store_manager_api.dto.InventoryResponseDTO
import com.rtr.store_manager_api.service.InventoryService
import com.rtr.store_manager_api.util.HeaderValidator
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
        HeaderValidator.validateUserId(userId)
        val created = inventoryService.createInventory(dto, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<InventoryResponseDTO>> =
        ResponseEntity.ok(inventoryService.getAllInventory())

    @GetMapping("/{productId}")
    fun getById(@PathVariable productId: String): ResponseEntity<InventoryResponseDTO> =
        ResponseEntity.ok(inventoryService.getInventoryByProductId(productId))

    @PatchMapping("/{product_id}")
    fun patch(
        @PathVariable("product_id") productId: String,
        @RequestBody dto: InventoryRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<InventoryResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val updated = inventoryService.updateInventory(productId, dto, userId)
        return ResponseEntity.status(HttpStatus.OK).body(updated)
    }

    @DeleteMapping("/{product_id}")
    fun delete(
        @PathVariable("product_id") productId: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        HeaderValidator.validateUserId(userId)
        inventoryService.deleteInventory(productId, userId)
        return ResponseEntity.noContent().build()
    }
}
