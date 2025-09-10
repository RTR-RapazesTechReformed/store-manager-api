package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_role")
data class UserRole(
    @Id
    @Column(length = 3)
    val id: String,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permission", joinColumns = [JoinColumn(name = "role_id")])
    @Column(name = "permission")
    var permissions: MutableSet<String> = mutableSetOf(),

): BaseEntity()
