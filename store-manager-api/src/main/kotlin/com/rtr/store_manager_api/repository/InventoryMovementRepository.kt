package com.rtr.store_manager_api.repository
import  com.rtr.store_manager_api.domain.entity.InventoryMovement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface InventoryMovementRepository : JpaRepository<InventoryMovement, String> {
    fun findAllByDeletedFalse(): List<InventoryMovement>
    fun findByIdAndDeletedFalse(id: String): Optional<InventoryMovement>
}