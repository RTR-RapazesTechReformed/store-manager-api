package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.UserRequestDTO
import com.rtr.store_manager_api.dto.UserResponseDTO
import com.rtr.store_manager_api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody user: UserRequestDTO, @RequestHeader("user-id") userId: String): ResponseEntity<UserResponseDTO> {
        val createdUser = userService.createUser(user, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @GetMapping
    fun getAllUsers(
        @RequestParam(required = false) username: String?,
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) role: String?
    ): ResponseEntity<List<UserResponseDTO>> {
        val users = userService.getAllUsers(username, email, role)
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserResponseDTO> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String,
        @RequestBody user: UserRequestDTO
    ): ResponseEntity<UserResponseDTO> {
        val updatedUser = userService.updateUser(id, user, userId)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @RequestHeader("user-id") userId: String,
        @PathVariable id: String
    ): ResponseEntity<Void> {
        userService.deleteUser(id, userId)
        return ResponseEntity.noContent().build()
    }
}
