package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StoreRepository : JpaRepository<Store, String> {
    fun findAllByDeletedFalse(): List<Store>
    fun findByIdAndDeletedFalse(id: String): Optional<Store>
}
