package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.PermissionRequestDTO
import com.rtr.store_manager_api.dto.PermissionResponseDTO
import com.rtr.store_manager_api.service.PermissionService
import com.rtr.store_manager_api.util.HeaderValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/permissions")
class PermissionController(
    private val permissionService: PermissionService
) {

    @PostMapping
    fun createPermission(
        @RequestBody dto: PermissionRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<PermissionResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val created = permissionService.createPermission(dto, userId)

        return  ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAllPermissions(): ResponseEntity<List<PermissionResponseDTO>> {
        val list = permissionService.getAllPermissions()
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getPermissionById(@PathVariable id: String): ResponseEntity<PermissionResponseDTO> {
        val dto = permissionService.getPermissionById(id)
        return ResponseEntity.ok(dto)
    }

    @PutMapping("/{id}")
    fun updatePermission(
        @PathVariable id: String,
        @RequestBody dto: PermissionRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<PermissionResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val updated = permissionService.updatePermission(id, dto, userId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deletePermission(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        HeaderValidator.validateUserId(userId)
        permissionService.deletePermission(id, userId)
        return ResponseEntity.noContent().build()
    }
}
