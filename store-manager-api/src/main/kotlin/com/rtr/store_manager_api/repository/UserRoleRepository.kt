package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRoleRepository : JpaRepository<UserRole, String> {
    fun findByName(name: String): UserRole?
}