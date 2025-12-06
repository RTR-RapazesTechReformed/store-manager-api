package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.Inventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface InventoryRepository : JpaRepository<Inventory, String> {
    fun findAllByDeletedFalseAndProduct_DeletedFalse(): List<Inventory>
    fun findByProductIdAndDeletedFalse(productId: String): Optional<Inventory>

}
