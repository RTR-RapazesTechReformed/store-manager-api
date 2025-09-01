package com.rtr.store_manager_api.repository

import  com.rtr.store_manager_api.domain.entity.Card
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<Card, String> {
}