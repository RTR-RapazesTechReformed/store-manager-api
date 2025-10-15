package com.rtr.store_manager_api.domain.entity

import at.favre.lib.crypto.bcrypt.BCrypt
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user")
data class User(
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(name = "password_hash", nullable = false)
    var password: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = true, columnDefinition = "CHAR(36)")
    var store: Store? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    var role: UserRole
) : BaseEntity()
{
    fun encodePassword(rawPassword: String) {
        this.password = BCrypt.withDefaults()
            .hashToString(12, rawPassword.toCharArray())
    }

    fun checkPassword(rawPassword: String): Boolean {
        return BCrypt.verifyer()
            .verify(rawPassword.toCharArray(), this.password)
            .verified
    }

}