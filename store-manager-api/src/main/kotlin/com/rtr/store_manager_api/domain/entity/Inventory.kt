package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "inventory")
data class Inventory(
    @Id
    @Column(name = "product_id")
    val productId: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id")
    val product: Product,

    @Column(nullable = false)
    var quantity: Int = 0
) : BaseEntity()
