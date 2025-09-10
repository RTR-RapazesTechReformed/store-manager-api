package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.UserRoleRequestDTO
import com.rtr.store_manager_api.dto.UserRoleResponseDTO
import com.rtr.store_manager_api.service.UserRoleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/roles")
class UserRoleController(
    private val userRoleService: UserRoleService
) {

    @PostMapping
    fun createRole(
        @RequestHeader("user-id") userId: String,
        @RequestBody dto: UserRoleRequestDTO
    ): ResponseEntity<UserRoleResponseDTO> {
        val role = userRoleService.createRole(dto, userId)
        return ResponseEntity.ok(role)
    }

    @GetMapping
    fun getAllRoles(): ResponseEntity<List<UserRoleResponseDTO>> {
        val roles = userRoleService.getAllRoles()
        return ResponseEntity.ok(roles)
    }

    @GetMapping("/{id}")
    fun getRoleById(@PathVariable id: String): ResponseEntity<UserRoleResponseDTO> {
        val role = userRoleService.getRoleById(id)
        return ResponseEntity.ok(role)
    }

    @PutMapping("/{id}")
    fun updateRole(
        @RequestHeader("user-id") userId: String,
        @PathVariable id: String,
        @RequestBody dto: UserRoleRequestDTO
    ): ResponseEntity<UserRoleResponseDTO> {
        val role = userRoleService.updateRole(id, dto, userId)
        return ResponseEntity.ok(role)
    }

    @DeleteMapping("/{id}")
    fun deleteRole(
        @RequestHeader("user-id") userId: String,
        @PathVariable id: String
    ): ResponseEntity<Void> {
        userRoleService.deleteRole(id, userId)
        return ResponseEntity.noContent().build()
    }
}
