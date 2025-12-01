package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ProductRepository : JpaRepository<Product, String> {
    fun findAllByDeletedFalse(): List<Product>
    fun findByIdAndDeletedFalse(id: String): Optional<Product>
}