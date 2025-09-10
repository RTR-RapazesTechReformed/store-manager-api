package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
    fun existsByRoleIdAndDeletedFalse(roleId: String): Boolean
}