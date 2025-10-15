package com.rtr.store_manager_api.service

import com.rtr.store_manager_api.dto.StoreRequestDTO
import com.rtr.store_manager_api.dto.StoreResponseDTO
import com.rtr.store_manager_api.dto.StoreUpdateDTO

interface StoreService {
    fun createStore(storeInput: StoreRequestDTO, userId: String): StoreResponseDTO
    fun getAllStores(): List<StoreResponseDTO>
    fun getStoreById(id: String): StoreResponseDTO?
    fun updateStore(id: String, store: StoreUpdateDTO, userId: String): StoreResponseDTO?
    fun deleteStore(id: String, userId: String): Boolean
}