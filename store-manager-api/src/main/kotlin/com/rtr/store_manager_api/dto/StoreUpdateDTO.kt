package com.rtr.store_manager_api.dto

data class StoreUpdateDTO (
    val name: String? = null,
    val cep: String? = null,
    val number: String? = null,
    val complement: String? = null,
)