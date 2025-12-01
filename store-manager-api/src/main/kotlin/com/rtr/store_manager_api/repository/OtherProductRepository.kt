package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.OtherProduct
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface OtherProductRepository : JpaRepository<OtherProduct, String> {
    fun findAllByDeletedFalse(): List<OtherProduct>
    fun findByIdAndDeletedFalse(id: String): Optional<OtherProduct>
}
