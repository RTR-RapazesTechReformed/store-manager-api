package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.InventoryMovement
import com.rtr.store_manager_api.domain.enum.MovementType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface InventoryMovementRepository : JpaRepository<InventoryMovement, String> {
}