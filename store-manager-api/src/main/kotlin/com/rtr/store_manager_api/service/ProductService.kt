package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.ProductRequestDTO
import com.rtr.store_manager_api.dto.ProductResponseDTO
import com.rtr.store_manager_api.dto.ProductUpdateDTO

interface ProductService {
    fun createProduct(dto: ProductRequestDTO, userId: String): ProductResponseDTO
    fun getAllProducts(storeId: String?): List<ProductResponseDTO>
    fun getProductById(id: String): ProductResponseDTO
    fun updateProduct(id: String, dto: ProductUpdateDTO, userId: String): ProductResponseDTO
    fun deleteProduct(id: String, userId: String)
}