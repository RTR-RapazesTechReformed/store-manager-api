package com.rtr.store_manager_api.domain.entity

import  com.rtr.store_manager_api.domain.enum.CardRarity
import com.rtr.store_manager_api.domain.enum.PokemonType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "card")
data class Card(
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    var title: String,

    @Column(name = "season")
    var season: String? = null,

    @Column(name = "nationality")
    var nationality: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "pokemon_type", nullable = false, length = 20)
    var pokemonType: PokemonType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    var collection: Collection,

    @Column(nullable = false)
    var code: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rarity: CardRarity = CardRarity.COMMON
) : BaseEntity()