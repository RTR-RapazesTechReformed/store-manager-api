package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "inventory")
data class Inventory(
    @Id
    @Column(name = "product_id")
    val productId: String,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id")
    val product: Product,

    @Column(nullable = false)
    var quantity: Int = 0
) : BaseEntity()
