package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.OtherProduct
import org.springframework.data.jpa.repository.JpaRepository

interface OtherProductRepository : JpaRepository<OtherProduct, String> {

}
