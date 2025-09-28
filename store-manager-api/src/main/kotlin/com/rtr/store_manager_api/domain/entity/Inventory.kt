package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "inventory")
data class Inventory(
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    val product: Product,

    @Column(nullable = false)
    var quantity: Int
) : BaseEntity() {
    @Id
    @Column(name = "product_id", length = 36)
    var productId: String? = null
}
