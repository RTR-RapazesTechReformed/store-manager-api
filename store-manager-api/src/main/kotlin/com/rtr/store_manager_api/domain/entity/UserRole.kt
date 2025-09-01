package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_role")
data class UserRole(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val description: String
) : BaseEntity()
