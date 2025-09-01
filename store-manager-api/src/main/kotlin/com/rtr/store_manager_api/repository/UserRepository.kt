package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
}