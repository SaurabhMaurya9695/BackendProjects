package com.backend.kafka.api;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class OrderPublisher {

    private static final String ORDERS_TOPIC = "orders.v1";

    private final Producer<String, String> orderProducer;

    public OrderPublisher(Producer<String, String> orderProducer) {
        this.orderProducer = orderProducer;
    }

    public OrderPublication publish(JsonNode orderEvent) {
        validateOrderEvent(orderEvent);

        String orderId = orderEvent.get("orderId").asText();
        ProducerRecord<String, String> orderRecord = new ProducerRecord<>(ORDERS_TOPIC, orderId, orderEvent.toString());

        try {
            RecordMetadata metadata = orderProducer.send(orderRecord).get();
            return new OrderPublication(orderId, metadata.topic(), metadata.partition(), metadata.offset());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new OrderPublishException("Publishing order was interrupted", exception);
        } catch (ExecutionException exception) {
            throw new OrderPublishException("Failed to publish order " + orderId, exception.getCause());
        }
    }

    private static void validateOrderEvent(JsonNode orderEvent) {
        if (orderEvent == null || !orderEvent.isObject()) {
            throw new InvalidOrderException("Order request must be a JSON object");
        }

        JsonNode orderId = orderEvent.get("orderId");
        if (orderId == null || !orderId.isTextual() || orderId.asText().isBlank()) {
            throw new InvalidOrderException("Order request must contain a non-empty string orderId");
        }
    }

    public record OrderPublication(String orderId, String topic, int partition, long offset) {

    }

    public static class InvalidOrderException extends RuntimeException {

        public InvalidOrderException(String message) {
            super(message);
        }
    }

    public static class OrderPublishException extends RuntimeException {

        public OrderPublishException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
