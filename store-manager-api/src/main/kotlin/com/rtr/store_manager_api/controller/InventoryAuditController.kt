package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.InventoryAuditResponseDTO
import com.rtr.store_manager_api.service.AuditService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/store-manager-api/inventory-audits")
class InventoryAuditController (
    private val inventoryAuditService: AuditService
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<InventoryAuditResponseDTO>> {
        val movements = inventoryAuditService.getAllMovements()
        return ResponseEntity.ok(movements)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<InventoryAuditResponseDTO> {
        val movement = inventoryAuditService.getMovementById(id)
        return ResponseEntity.ok(movement)
    }
}