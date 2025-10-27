package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.OtherProduct
import com.rtr.store_manager_api.dto.OtherProductRequestDTO
import com.rtr.store_manager_api.dto.OtherProductResponseDTO
import com.rtr.store_manager_api.repository.OtherProductRepository
import com.rtr.store_manager_api.service.OtherProductService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class OtherProductServiceImpl(
    private val otherProductRepository: OtherProductRepository
) : OtherProductService {

    override fun createOtherProduct(dto: OtherProductRequestDTO, userId: String): OtherProductResponseDTO {
        val otherProduct = OtherProduct(
            type = dto.type,
            nationality = dto.nationality,
            packageContents = dto.packageContents,
            extraInfo = dto.extraInfo
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        return otherProductRepository.save(otherProduct).toResponseDTO()
    }

    override fun getAllOtherProducts(): List<OtherProductResponseDTO> {
        return otherProductRepository.findAll()
            .filter { !it.deleted }
            .map { it.toResponseDTO() }
    }

    override fun getOtherProductById(id: String): OtherProductResponseDTO {
        val otherProduct = otherProductRepository.findById(id)
            .orElseThrow { NoSuchElementException("OtherProduct $id não encontrado") }

        return otherProduct.toResponseDTO()
    }

    override fun updateOtherProduct(id: String, dto: OtherProductRequestDTO, userId: String): OtherProductResponseDTO {
        val existing = otherProductRepository.findById(id)
            .orElseThrow { NoSuchElementException("OtherProduct $id não encontrado") }

        existing.type = dto.type ?: existing.type
        existing.nationality = dto.nationality ?: existing.nationality
        existing.packageContents = dto.packageContents ?: existing.packageContents
        existing.extraInfo = dto.extraInfo ?: existing.extraInfo
        existing.updatedBy = userId
        existing.updatedAt = LocalDateTime.now()

        return otherProductRepository.save(existing).toResponseDTO()
    }


    override fun deleteOtherProduct(id: String, userId: String) {
        val existing = otherProductRepository.findById(id)
            .orElseThrow { NoSuchElementException("OtherProduct $id não encontrado") }

        existing.deleted = true
        existing.updatedBy = userId
        existing.updatedAt = LocalDateTime.now()

        otherProductRepository.save(existing)
    }

    private fun OtherProduct.toResponseDTO() = OtherProductResponseDTO(
        id = this.id,
        type = this.type,
        nationality = this.nationality,
        packageContents = this.packageContents,
        extraInfo = this.extraInfo,
        createdBy = this.createdBy,
        createdAt = this.createdAt,
        updatedBy = this.updatedBy,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}
