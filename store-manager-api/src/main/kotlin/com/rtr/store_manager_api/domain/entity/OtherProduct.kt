package com.rtr.store_manager_api.domain.entity

import com.rtr.store_manager_api.domain.enum.OtherProductType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "other_product")
data class OtherProduct(
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: OtherProductType,

    @Column(name = "nationality")
    var nationality: String? = null,

    @Column(name = "package_contents")
    var packageContents: String? = null,

    @Column(name = "extra_info")
    var extraInfo: String? = null
) : BaseEntity()
