package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.UserRequestDTO
import com.rtr.store_manager_api.dto.UserUpdateDTO
import com.rtr.store_manager_api.dto.UserResponseDTO
import com.rtr.store_manager_api.service.UserService
import com.rtr.store_manager_api.util.HeaderValidator
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(
        @Valid @RequestBody user: UserRequestDTO,
        @RequestHeader("user-id") userId: String): ResponseEntity<UserResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val createdUser = userService.createUser(user, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @GetMapping
    fun getAllUsers(
        @RequestParam(required = false) username: String?,
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) role: String?,
        @RequestParam(required = false) storeId: String?
    ): ResponseEntity<List<UserResponseDTO>> {
        val users = userService.getAllUsers(username, email, storeId, role)
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
        @Valid @RequestBody user: UserUpdateDTO
    ): ResponseEntity<UserResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val updatedUser = userService.updateUser(id, user, userId)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @RequestHeader("user-id") userId: String,
        @PathVariable id: String
    ): ResponseEntity<Void> {
        HeaderValidator.validateUserId(userId)
        userService.deleteUser(id, userId)
        return ResponseEntity.noContent().build()
    }
}
