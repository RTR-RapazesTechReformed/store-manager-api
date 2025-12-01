package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PermissionRepository : JpaRepository<Permission, String> {
    fun findByName(name: String): Permission?
    fun findAllByDeletedFalse(): List<Permission>
    fun findByIdAndDeletedFalse(id: String): Optional<Permission>
}