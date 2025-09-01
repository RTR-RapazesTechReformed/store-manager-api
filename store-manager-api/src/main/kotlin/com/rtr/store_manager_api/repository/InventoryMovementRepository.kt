package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.InventoryMovement
import org.springframework.data.jpa.repository.JpaRepository

interface InventoryMovementRepository : JpaRepository<InventoryMovement, String> {
}