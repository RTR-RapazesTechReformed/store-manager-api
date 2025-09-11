package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PermissionRepository : JpaRepository<Permission, String> {
    fun findByName(name: String): Permission?
}