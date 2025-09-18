package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "collection")
data class Collection(
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    val abbreviation: String? = null,

    @Column(name = "release_date")
    val releaseDate: LocalDate? = null,

    val generation: String? = null
) : BaseEntity()
