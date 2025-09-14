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
    val title: String,

    val artistName: String? = null,

    val season: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    val collection: Collection,

    @Column(nullable = false)
    val code: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val rarity: CardRarity = CardRarity.COMMON
) : BaseEntity()