package com.rtr.store_manager_api.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
abstract class BaseEntity {
    @Column(name = "created_by")
    var createdBy: String? = null

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_by")
    var updatedBy: String? = null

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "deleted")
    var deleted: Boolean = false
}
