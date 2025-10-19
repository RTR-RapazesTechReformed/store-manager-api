package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.InventoryMovementRequestDTO
import com.rtr.store_manager_api.dto.InventoryMovementResponse
import com.rtr.store_manager_api.dto.InventoryMovementResponseDTO
import com.rtr.store_manager_api.dto.InventoryMovementUpdateDTO
import com.rtr.store_manager_api.service.InventoryMovementService
import com.rtr.store_manager_api.util.HeaderValidator
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
    ): ResponseEntity<InventoryMovementResponse> {
        HeaderValidator.validateUserId(userId)
        val created = inventoryMovementService.createMovement(dto, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<InventoryMovementResponseDTO>> {
        val movements = inventoryMovementService.getAllMovements()
        return ResponseEntity.ok(movements)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<InventoryMovementResponseDTO> {
        val movement = inventoryMovementService.getMovementById(id)
        return ResponseEntity.ok(movement)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody dto: InventoryMovementUpdateDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<InventoryMovementResponse> {
        HeaderValidator.validateUserId(userId)
        val updated = inventoryMovementService.updateMovement(id, dto, userId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<InventoryMovementResponse> {
        HeaderValidator.validateUserId(userId)
        val deleted = inventoryMovementService.deleteMovement(id, userId)
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deleted)
    }
}