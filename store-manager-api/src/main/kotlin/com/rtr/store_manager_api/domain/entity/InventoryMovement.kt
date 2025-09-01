package com.rtr.store_manager_api.domain.entity

import com.rtr.store_manager_api.domain.enum.MovementType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "inventory_movement")
data class InventoryMovement(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val quantity: Int, // positivo = entrada, negativo = saída

    val unitPurchasePrice: Double? = null,

    val unitSalePrice: Double? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: MovementType,

    val description: String? = null
) : BaseEntity()
