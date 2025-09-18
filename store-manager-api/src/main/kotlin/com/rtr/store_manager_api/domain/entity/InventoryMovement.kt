package com.rtr.store_manager_api.domain.entity

import com.rtr.store_manager_api.domain.enum.MovementType
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "inventory_movement")
data class InventoryMovement(
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val quantity: Int, // positivo = entrada, negativo = saída

    @Column(name = "unit_purchase_price", precision = 15, scale = 2)
    var unitPurchasePrice: BigDecimal? = null,

    @Column(name = "unit_sale_price", precision = 15, scale = 2)
    var unitSalePrice: BigDecimal? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: MovementType,

    val description: String? = null
) : BaseEntity()
