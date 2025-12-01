package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.domain.entity.Store
import com.rtr.store_manager_api.dto.StoreRequestDTO
import com.rtr.store_manager_api.dto.StoreResponseDTO
import com.rtr.store_manager_api.dto.StoreUpdateDTO
import com.rtr.store_manager_api.exception.RtrRuleException
import com.rtr.store_manager_api.repository.StoreRepository
import com.rtr.store_manager_api.service.StoreService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository
) : StoreService {

    override fun createStore(storeInput: StoreRequestDTO, userId: String): StoreResponseDTO {
        val store = Store(
            name = storeInput.name,
            cep = storeInput.cep,
            number = storeInput.number,
            complement = storeInput.complement
        ).apply {
            createdBy = userId
            updatedBy = userId
        }

        return storeRepository.save(store).toResponseDTO()
    }

    override fun getAllStores(): List<StoreResponseDTO> =
        storeRepository.findAllByDeletedFalse().map { it.toResponseDTO() }

    override fun getStoreById(id: String): StoreResponseDTO? =
        storeRepository.findByIdAndDeletedFalse(id)
            .map { it.toResponseDTO() }
            .orElse(null)

    override fun updateStore(id: String, store: StoreUpdateDTO, userId: String): StoreResponseDTO? {
        val existing = storeRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { NoSuchElementException("Loja $id não encontrada") }

        store.name?.let {
            require(it.isNotBlank()) { "Nome não pode estar vazio" }
            require(it.length in 2..200) { "Nome deve ter entre 2 e 200 caracteres" }
            existing.name = it.trim()
        }

        store.cep?.let {
            require(it.isNotBlank()) { "CEP não pode estar vazio" }
            require(it.length == 8) { "CEP deve ter 8 caracteres" }
            existing.cep = it.trim()
        }

        store.number?.let {
            require(it.isNotBlank()) { "Número não pode estar vazio" }
            existing.number = it.trim()
        }

        store.complement?.let {
            existing.complement = it.trim()
        }

        existing.updatedAt = LocalDateTime.now()
        existing.updatedBy = userId

        return storeRepository.save(existing).toResponseDTO()
    }

    override fun deleteStore(id: String, userId: String): Boolean {
        val store = storeRepository.findByIdAndDeletedFalse(id)
            .orElseThrow { NoSuchElementException("Loja $id não encontrada") }

        store.deleted = true
        store.updatedBy = userId
        store.updatedAt = LocalDateTime.now()

        storeRepository.save(store)
        return true
    }

    private fun Store.toResponseDTO() = StoreResponseDTO(
        id = this.id,
        name = this.name,
        cep = this.cep,
        complement = this.complement,
        number = this.number,
        createdBy = this.createdBy,
        updatedBy = this.updatedBy,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}
