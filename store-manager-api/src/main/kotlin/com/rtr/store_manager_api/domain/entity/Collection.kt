package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "collection")
data class Collection(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    val abbreviation: String? = null,

    @Column(name = "release_date")
    val releaseDate: LocalDate? = null
) : BaseEntity()