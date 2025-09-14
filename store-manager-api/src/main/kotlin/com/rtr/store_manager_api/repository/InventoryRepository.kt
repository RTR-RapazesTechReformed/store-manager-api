package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.Inventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InventoryRepository : JpaRepository<Inventory, String> {
    fun findByProductId(productId: String): Inventory?
}
