package com.rtr.store_manager_api.domain.entity

import com.rtr.store_manager_api.domain.enum.ProductCondition
import com.rtr.store_manager_api.domain.enum.ProductType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "product")
data class Product(
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    val description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = true, updatable = false, columnDefinition = "CHAR(36)")
    val card: Card? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_product_id")
    val otherProduct: OtherProduct? = null,

    @Column(nullable = false)
    val price: Double = 0.0,

    @Enumerated(EnumType.STRING)
    @Column(name = "product_condition", nullable = false)
    val condition: ProductCondition = ProductCondition.MINT
) : BaseEntity()