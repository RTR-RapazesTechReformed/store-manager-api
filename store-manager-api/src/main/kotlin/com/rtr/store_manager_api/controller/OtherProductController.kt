package com.rtr.store_manager_api.controller

import com.rtr.store_manager_api.dto.OtherProductRequestDTO
import com.rtr.store_manager_api.dto.OtherProductResponseDTO
import com.rtr.store_manager_api.service.OtherProductService
import com.rtr.store_manager_api.util.HeaderValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/store-manager-api/other-products")
class OtherProductController(
    private val otherProductService: OtherProductService
) {

    @PostMapping
    fun create(
        @RequestBody dto: OtherProductRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<OtherProductResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val created = otherProductService.createOtherProduct(dto, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<OtherProductResponseDTO> {
        val product = otherProductService.getOtherProductById(id)
        return ResponseEntity.ok(product)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<OtherProductResponseDTO>> {
        val products = otherProductService.getAllOtherProducts()
        return ResponseEntity.ok(products)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody dto: OtherProductRequestDTO,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<OtherProductResponseDTO> {
        HeaderValidator.validateUserId(userId)
        val updated = otherProductService.updateOtherProduct(id, dto, userId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: String,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<Void> {
        HeaderValidator.validateUserId(userId)
        otherProductService.deleteOtherProduct(id, userId)
        return ResponseEntity.noContent().build()
    }
}
