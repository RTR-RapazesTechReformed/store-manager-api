package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, String> {
}