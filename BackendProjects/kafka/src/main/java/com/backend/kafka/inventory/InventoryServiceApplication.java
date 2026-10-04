package com.backend.kafka.inventory;

import com.backend.kafka.config.KafkaClientProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InventoryServiceApplication {

    private static final String ORDERS_TOPIC = "orders.v1";
    private static final String INVENTORY_EVENTS_TOPIC = "inventory-events.v1";
    private static final String CONSUMER_GROUP = "inventory-service";
    private static final String TRANSACTIONAL_ID = "inventory-service-instance-1";

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        InventoryStateStore stateStore = new InventoryStateStore(Path.of("inventory-state.json"));
        InventoryStateStore.InventoryState state = stateStore.load();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
                KafkaClientProperties.consumerProperties(CONSUMER_GROUP));
                KafkaProducer<String, String> producer = new KafkaProducer<>(
                        KafkaClientProperties.transactionalProducerProperties(TRANSACTIONAL_ID))) {
            producer.initTransactions();
            consumer.subscribe(Collections.singletonList(ORDERS_TOPIC));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                if (records.isEmpty()) {
                    continue;
                }

                producer.beginTransaction();
                try {
                    for (ConsumerRecord<String, String> record : records) {
                        publishInventoryEvent(objectMapper, producer, record, state);
                    }
                    producer.sendOffsetsToTransaction(offsetsFor(records), consumer.groupMetadata());
                    producer.commitTransaction();
                    stateStore.save(state);
                    System.out.println("Kafka transaction committed");
                } catch (Exception exception) {
                    producer.abortTransaction();
                    state = stateStore.load();
                    System.err.println("Kafka transaction aborted");
                    exception.printStackTrace();
                }
            }
        }
    }

    private static void publishInventoryEvent(ObjectMapper objectMapper, KafkaProducer<String, String> producer,
            ConsumerRecord<String, String> record, InventoryStateStore.InventoryState state) throws Exception {
        JsonNode orderEvent = objectMapper.readTree(record.value());
        String productId = orderEvent.path("productId").asText(null);
        int quantity = orderEvent.path("quantity").asInt(-1);
        if (productId == null || quantity < 0) {
            throw new IllegalArgumentException("Order event must contain productId and quantity");
        }

        boolean reserved = reserveInventory(state.availableStock(), productId, quantity);
        producer.send(new ProducerRecord<>(INVENTORY_EVENTS_TOPIC, record.key(),
                createInventoryEvent(record, productId, quantity, reserved))).get();
    }

    private static Map<TopicPartition, OffsetAndMetadata> offsetsFor(ConsumerRecords<String, String> records) {
        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        for (TopicPartition partition : records.partitions()) {
            long nextOffset = records.records(partition).get(records.records(partition).size() - 1).offset() + 1;
            offsets.put(partition, new OffsetAndMetadata(nextOffset));
        }
        return offsets;
    }

    static boolean reserveInventory(Map<String, Integer> stock, String productId, int quantity) {
        int current = stock.getOrDefault(productId, 0);
        if (current < quantity) {
            return false;
        }
        stock.put(productId, current - quantity);
        return true;
    }

    static String createInventoryEvent(ConsumerRecord<String, String> record, String productId, int quantity,
            boolean reserved) {
        String eventType = reserved ? "InventoryReserved" : "InventoryRejected";
        return "{\"eventId\":\"inventory-event-" + record.partition() + "-" + record.offset() + "\",\"eventType\":\""
                + eventType + "\",\"eventVersion\":1,\"orderId\":\"" + record.key() + "\",\"productId\":\"" + productId
                + "\",\"quantity\":" + quantity + "}";
    }
}
