package com.backend.kafka.order;

import com.backend.kafka.config.KafkaClientProperties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class OrderServiceApplication {

    private static final String ORDERS_TOPIC = "orders.v1";

    public static void main(String[] args) {
        try (KafkaProducer<String, String> orderProducer = new KafkaProducer<>(KafkaClientProperties.producerProperties())) {
            String orderId = "order-102";
            ProducerRecord<String, String> orderCreatedEvent = getProducerRecord(orderId);
            Callback callback = (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Failed to publish order " + orderId);
                    exception.printStackTrace();
                    return;
                }

                System.out.printf("Order published: topic=%s, partition=%d, offset=%d%n", metadata.topic(),
                        metadata.partition(), metadata.offset());
            };

            orderProducer.send(orderCreatedEvent, callback);
            orderProducer.flush();
        }
    }

    private static ProducerRecord<String, String> getProducerRecord(String orderId) {
        String eventJson = """
                        {
                            "eventId": "event-009",
                            "eventType": "OrderCreated",
                            "orderId": "order-102",
                            "eventVersion": 1,
                            "customerId": "customer-8",
                            "amount": 149.99,
                            "productId": "product-1",
                            "quantity": 8
                        }
                        """;
        return new ProducerRecord<>(ORDERS_TOPIC, orderId, eventJson);
    }
}
