package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.InventoryAudit
import com.rtr.store_manager_api.domain.enum.AuditStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional

@Repository
interface InventoryAuditRepository : JpaRepository<InventoryAudit, String> {
    fun findByTimestampBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<InventoryAudit>
    fun findByStatusOrderByTimestampDesc(status: AuditStatus): List<InventoryAudit>
}