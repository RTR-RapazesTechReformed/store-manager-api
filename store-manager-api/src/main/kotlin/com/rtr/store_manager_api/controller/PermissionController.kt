package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.PermissionRequestDTO
import com.rtr.store_manager_api.dto.PermissionResponseDTO
import com.rtr.store_manager_api.service.PermissionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/permissions")
class PermissionController(
    private val permissionService: PermissionService
) {

    @PostMapping
    fun createPermission(
        @RequestBody dto: PermissionRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<PermissionResponseDTO> {
        val created = permissionService.createPermission(dto, userId)
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id)
            .toUri()
        return ResponseEntity.created(location).body(created)
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
        val updated = permissionService.updatePermission(id, dto, userId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deletePermission(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        permissionService.deletePermission(id, userId)
        return ResponseEntity.noContent().build()
    }
}
