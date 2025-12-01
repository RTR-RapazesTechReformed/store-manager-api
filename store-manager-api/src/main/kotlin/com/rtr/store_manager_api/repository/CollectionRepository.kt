package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.Collection
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CollectionRepository : JpaRepository<Collection, String> {
    fun findAllByDeletedFalse(): List<Collection>
    fun findByIdAndDeletedFalse(id: String): Optional<Collection>
}