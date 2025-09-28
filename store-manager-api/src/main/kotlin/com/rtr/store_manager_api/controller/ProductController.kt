package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.service.ProductService
import com.rtr.store_manager_api.dto.ProductRequestDTO
import com.rtr.store_manager_api.dto.ProductResponseDTO
import com.rtr.store_manager_api.dto.ProductUpdateDTO
import com.rtr.store_manager_api.util.HeaderValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    fun create(
        @RequestBody dto: ProductRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<ProductResponseDTO> {
        val created = productService.createProduct(dto, userId)
        HeaderValidator.validateUserId(userId)

        return  ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<ProductResponseDTO>> =
        ResponseEntity.ok(productService.getAllProducts())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<ProductResponseDTO> =
        ResponseEntity.ok(productService.getProductById(id))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody dto: ProductUpdateDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<ProductResponseDTO> {
        HeaderValidator.validateUserId(userId)
        return ResponseEntity.ok(productService.updateProduct(id, dto, userId))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        productService.deleteProduct(id, userId)
        HeaderValidator.validateUserId(userId)
        return ResponseEntity.noContent().build()
    }
}
