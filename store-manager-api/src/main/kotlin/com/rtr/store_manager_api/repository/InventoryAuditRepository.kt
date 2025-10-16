package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.InventoryAudit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InventoryAuditRepository : JpaRepository<InventoryAudit, String>