package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.UserRequestDTO
import com.rtr.store_manager_api.dto.UserResponseDTO

interface UserService {
    fun createUser(userInput: UserRequestDTO): UserResponseDTO
    fun getAllUsers(name: String?, email: String?, role: String?): List<UserResponseDTO>
    fun getUserById(id: String): UserResponseDTO?
    fun updateUser(id: String, user: UserRequestDTO, userId: String): UserResponseDTO?
    fun deleteUser(id: String, userId: String): Boolean
    fun validateUser(email: String, rawPassword: String): UserResponseDTO
}
