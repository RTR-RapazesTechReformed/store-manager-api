package com.rtr.store_manager_api.domain.entity

import  com.rtr.store_manager_api.domain.enum.CardRarity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "card")
data class Card(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    var title: String,

    var artistName: String? = null,

    var season: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    var collection: Collection,

    @Column(nullable = false)
    var code: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rarity: CardRarity = CardRarity.COMMON
) : BaseEntity()