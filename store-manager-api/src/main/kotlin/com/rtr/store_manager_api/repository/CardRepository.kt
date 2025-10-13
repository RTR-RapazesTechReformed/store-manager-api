package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.Card
import com.rtr.store_manager_api.domain.enum.CardRarity
import com.rtr.store_manager_api.domain.enum.PokemonType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CardRepository : JpaRepository<Card, String> {
    @Query("""
    SELECT c FROM Card c
    WHERE (:collectionId IS NULL OR c.collection.id = :collectionId)
      AND (:pokemonType IS NULL OR c.pokemonType = :pokemonType)
      AND (:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:rarity IS NULL OR c.rarity = :rarity)
      AND (:nationality IS NULL OR c.nationality = :nationality)
      AND c.deleted = false
""")
    fun findWithFilters(
        collectionId: String?,
        pokemonType: PokemonType?,
        title: String?,
        rarity: CardRarity?,
        nationality: String?
    ): List<Card>

    @Query("""
        SELECT c FROM Card c
        WHERE c.collection.id = :collectionId
        AND (:rarity IS NULL OR c.rarity = :rarity)
        AND (:pokemonType IS NULL OR c.pokemonType = :pokemonType)
        AND (:nationality IS NULL OR c.nationality = :nationality)
        AND c.deleted = false
    """)
    fun findByCollectionWithFilters(
        collectionId: String,
        rarity: String?,
        pokemonType: String?,
        nationality: String?
    ): List<Card>
}