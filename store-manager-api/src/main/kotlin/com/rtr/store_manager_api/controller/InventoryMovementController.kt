package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.InventoryMovementRequestDTO
import com.rtr.store_manager_api.dto.InventoryMovementResponseDTO
import com.rtr.store_manager_api.service.InventoryMovementService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/inventory-movements")
class InventoryMovementController(
    private val inventoryMovementService: InventoryMovementService
) {

    @PostMapping
    fun create(
        @RequestBody dto: InventoryMovementRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<InventoryMovementResponseDTO> {
        val created = inventoryMovementService.createMovement(dto, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<InventoryMovementResponseDTO>> =
        ResponseEntity.ok(inventoryMovementService.getAllMovements())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<InventoryMovementResponseDTO> =
        ResponseEntity.ok(inventoryMovementService.getMovementById(id))

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        inventoryMovementService.deleteMovement(id, userId)
        return ResponseEntity.noContent().build()
    }
}
