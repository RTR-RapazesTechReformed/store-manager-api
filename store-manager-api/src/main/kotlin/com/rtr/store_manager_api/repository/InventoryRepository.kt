package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.Inventory
import org.springframework.data.jpa.repository.JpaRepository

interface InventoryRepository : JpaRepository<Inventory, String> {
}