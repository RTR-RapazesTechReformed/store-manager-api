package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "collection")
data class Collection(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    val abbreviation: String? = null,

    val releaseDate: LocalDateTime? = null
) : BaseEntity()
