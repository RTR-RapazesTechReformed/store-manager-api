package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.LoginRequestDTO
import com.rtr.store_manager_api.dto.LoginResponseDTO
import com.rtr.store_manager_api.dto.LogoutResponseDTO

interface AuthService {
    fun login(dto: LoginRequestDTO): LoginResponseDTO
    fun logout(sessionId: String): LogoutResponseDTO
}