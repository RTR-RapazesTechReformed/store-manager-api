package com.rtr.store_manager_api.dto

import jakarta.validation.constraints.Size

class StoreRequestDTO (
    @field:Size(min = 1, message = "Name cannot be blank")
    val name: String,

    @field:Size(min = 8, message = "CEP cannot be blank")
    val cep: String,

    @field:Size(min = 1, message = "Number cannot be blank")
    val number: String,

    val complement: String? = null,
)