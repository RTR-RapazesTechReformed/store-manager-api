package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.UserSession
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserSessionRepository : JpaRepository<UserSession, String> {
    fun findByUserIdAndActive(userId: UUID, active: Boolean): Optional<UserSession>
}