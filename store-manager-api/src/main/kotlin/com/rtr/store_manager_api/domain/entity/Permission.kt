package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "permission")
data class Permission(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var description: String

) : BaseEntity()
