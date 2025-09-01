package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user")
data class User(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    val role: UserRole
) : BaseEntity()
