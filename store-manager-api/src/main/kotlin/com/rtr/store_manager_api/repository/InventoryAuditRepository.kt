package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.InventoryAudit
import com.rtr.store_manager_api.domain.enum.AuditStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional

@Repository
interface InventoryAuditRepository : JpaRepository<InventoryAudit, String> {
    fun findByTimestampBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<InventoryAudit>
    fun findByStatusOrderByTimestampDesc(status: AuditStatus): List<InventoryAudit>

    @Query(
        """
        SELECT 
            ia.id,
            p.name AS product_name,
            COALESCE(u.name, ia.user_id) AS user_name,

            ia.movement_type,
            ia.quantity,
            ia.quantity_before,
            ia.quantity_after,

            im.unit_purchase_price,
            im.unit_sale_price,

            ia.timestamp,
            ia.operation,
            ia.status,
            ia.description,
            ia.error_message

        FROM inventory_audit ia
        LEFT JOIN product p ON p.id = ia.product_id
        LEFT JOIN inventory_movement im ON im.id = ia.movement_id
        LEFT JOIN user u ON u.id = ia.user_id
        """,
        nativeQuery = true
    )
    fun findAllWithNames(): List<Map<String, Any>>

    @Query(
        """
        SELECT 
            ia.id,
            p.name AS product_name,
            COALESCE(u.name, ia.user_id) AS user_name,

            ia.movement_type,
            ia.quantity,
            ia.quantity_before,
            ia.quantity_after,

            im.unit_purchase_price,
            im.unit_sale_price,

            ia.timestamp,
            ia.operation,
            ia.status,
            ia.description,
            ia.error_message

        FROM inventory_audit ia
        LEFT JOIN product p ON p.id = ia.product_id
        LEFT JOIN inventory_movement im ON im.id = ia.movement_id
        LEFT JOIN user u ON u.id = ia.user_id

        WHERE ia.id = :id
        """,
        nativeQuery = true
    )
    fun findByIdWithNames(id: String): Map<String, Any>?
}