package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.LoginRequestDTO
import com.rtr.store_manager_api.dto.LoginResponseDTO
import com.rtr.store_manager_api.dto.LogoutResponseDTO
import com.rtr.store_manager_api.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/store-manager-api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody dto: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val response = authService.login(dto)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

        @PostMapping("/logout/{sessionId}")
    fun logout(@PathVariable sessionId: String): ResponseEntity<LogoutResponseDTO> {
        val response = authService.logout(sessionId)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}