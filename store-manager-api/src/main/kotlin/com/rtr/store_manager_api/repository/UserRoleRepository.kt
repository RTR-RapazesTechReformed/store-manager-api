package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository


interface UserRoleRepository : JpaRepository<UserRole, String> {
}