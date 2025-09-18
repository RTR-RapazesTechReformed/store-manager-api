package com.rtr.store_manager_api.util

import java.util.UUID

object HeaderValidator {

    fun validateUserId(userId: String) {
        if (!isValidUUID(userId)) {
            throw IllegalArgumentException("Header 'user-id' must be a valid UUID")
        }
    }
    private fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
