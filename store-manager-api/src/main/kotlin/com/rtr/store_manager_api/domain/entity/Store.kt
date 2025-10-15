package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "store")
data class Store(

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var number: String,

    @Column(name = "cep", nullable = false, updatable = true, length = 8, columnDefinition = "CHAR(8)")
    var cep: String,

    @Column(nullable = true)
    var complement: String? = null,

) : BaseEntity()
