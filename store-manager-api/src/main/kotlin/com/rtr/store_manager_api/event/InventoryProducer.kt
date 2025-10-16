package com.rtr.store_manager_api.event

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class InventoryProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    fun sendMovement(message: InventoryMovementMessage) {
        kafkaTemplate.send("inventory-movement-topic", message.movementId, message)
    }

    fun sendToDLQ(errorMessage: InventoryMovementErrorMessage) {
        kafkaTemplate.send("inventory-movement-dlq", errorMessage)
    }
}
