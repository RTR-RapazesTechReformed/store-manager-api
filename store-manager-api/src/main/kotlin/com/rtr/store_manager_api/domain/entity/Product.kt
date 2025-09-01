package com.rtr.store_manager_api.domain.entity

import com.rtr.store_manager_api.domain.enum.ProductCondition
import com.rtr.store_manager_api.domain.enum.ProductType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "product")
data class Product(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    val description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: ProductType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    val card: Card? = null,

    @Column(nullable = false)
    val price: Double = 0.0,

    @Enumerated(EnumType.STRING)
    @Column(name = "product_condition", nullable = false)
    val condition: ProductCondition = ProductCondition.MINT
) : BaseEntity()
