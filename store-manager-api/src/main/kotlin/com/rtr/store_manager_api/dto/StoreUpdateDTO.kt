package com.rtr.store_manager_api.dto

import jakarta.validation.constraints.Size

data class StoreUpdateDTO (
    @field:Size(min = 1, message = "Name cannot be blank when provided")
    val name: String? = null,

    @field:Size(min = 8, message = "CEP cannot be blank")
    val cep: String? = null,

    @field:Size(min = 1, message = "Number cannot be blank")
    val number: String? = null,

    val complement: String? = null,
)