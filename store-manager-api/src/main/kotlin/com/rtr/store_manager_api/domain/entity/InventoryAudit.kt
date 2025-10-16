package com.rtr.store_manager_api.domain.entity

import com.rtr.store_manager_api.domain.enum.AuditStatus
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "inventory_audit")
data class InventoryAudit(
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movement_id", nullable = false)
    val movement: InventoryMovement,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false)
    val movementType: String,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false)
    val quantityBefore: Int,

    @Column(nullable = false)
    val quantityAfter: Int,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now(),

    val description: String? = null,

    @Column(nullable = true)
    val errorMessage: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: AuditStatus = AuditStatus.PROCESSED
)
