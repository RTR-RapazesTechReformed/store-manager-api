package com.rtr.store_manager_api.event

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory

@Component
class InventoryProducer(
    private val kafkaTemplate: KafkaTemplate<String, InventoryMovementMessage>,
    private val kafkaTemplateDlq: KafkaTemplate<String, InventoryMovementErrorMessage>
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun sendMovementMessage(message: InventoryMovementMessage) {
        try {
            kafkaTemplate.send("inventory-movement-topic", message.movementId, message)
                .whenComplete { result, ex ->
                    if (ex == null) {
                        logger.info("Mensagem enviada com sucesso: ${message.movementId}")
                    } else {
                        logger.error("Erro ao enviar mensagem para Kafka: ${message.movementId}", ex)
                        sendToDLQ(
                            InventoryMovementErrorMessage(
                                movementId = message.movementId,
                                productId = message.productId,
                                userId = message.userId,
                                quantity = message.quantity,
                                type = message.type,
                                operation = message.operation,
                                errorMessage = "Falha ao enviar para Kafka: ${ex.message}"
                            )
                        )
                    }
                }
        } catch (ex: Exception) {
            logger.error("Exceção ao enviar mensagem Kafka", ex)
            sendToDLQ(
                InventoryMovementErrorMessage(
                    movementId = message.movementId,
                    productId = message.productId,
                    userId = message.userId,
                    quantity = message.quantity,
                    type = message.type,
                    operation = message.operation,
                    errorMessage = "Exceção: ${ex.message}"
                )
            )
        }
    }

    fun sendToDLQ(errorMessage: InventoryMovementErrorMessage) {
        try {
            kafkaTemplateDlq.send("inventory-movement-dlq", errorMessage.movementId, errorMessage)
                .whenComplete { result, ex ->
                    if (ex == null) {
                        logger.warn("Mensagem de erro enviada para DLQ: ${errorMessage.movementId}")
                    } else {
                        logger.error("Erro crítico ao enviar para DLQ: ${errorMessage.movementId}", ex)
                    }
                }
        } catch (ex: Exception) {
            logger.error("Exceção ao enviar para DLQ", ex)
        }
    }
}