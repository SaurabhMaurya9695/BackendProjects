package com.backend.kafka.inventory;

import com.backend.kafka.config.KafkaClientProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InventoryServiceApplication {

    private static final String ORDERS_TOPIC = "orders.v1";
    private static final String INVENTORY_EVENTS_TOPIC = "inventory-events.v1";
    private static final String CONSUMER_GROUP = "inventory-service";

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        InventoryStateStore stateStore = new InventoryStateStore(Path.of("inventory-state.json"));
        InventoryStateStore.InventoryState savedState = stateStore.load();
        Map<String, Integer> availableStock = savedState.availableStock();
        Set<String> processedRecords = savedState.processedRecords();
        List<InventoryStateStore.OutboxEvent> outbox = savedState.outbox();

        try (KafkaConsumer<String, String> inventoryConsumer = new KafkaConsumer<>(
                KafkaClientProperties.consumerProperties(CONSUMER_GROUP));
                KafkaProducer<String, String> inventoryProducer = new KafkaProducer<>(
                        KafkaClientProperties.producerProperties())) {
            inventoryConsumer.subscribe(Collections.singletonList(ORDERS_TOPIC));
            while (true) {
                // 1 : Poll the records from Order Topic
                ConsumerRecords<String, String> records = inventoryConsumer.poll(Duration.ofMillis(500));
                try {
                    publishPendingEvents(inventoryProducer, outbox, stateStore, availableStock, processedRecords);
                } catch (Exception exception) {
                    System.err.println("Failed to publish pending inventory event");
                    exception.printStackTrace();
                }

                // 2 : Iterate through all records
                for (ConsumerRecord<String, String> record : records) {
                    // 3 : Print all values of records
                    System.out.printf("Received record: topic=%s, partition=%d, offset=%d, key=%s, value=%s%n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());

                    String recordIdentity = recordIdentity(record);
                    if (isProcessed(processedRecords, record)) {
                        System.out.println("Skipping already processed record " + recordIdentity);
                        continue;
                    }

                    String productId = null;
                    int requestedQuantity = -1;
                    boolean inventoryReserved = false;
                    try {
                        // 3.1 - get the full json here
                        JsonNode orderEvent = objectMapper.readTree(record.value());
                        productId = orderEvent.path("productId").asText(null);
                        requestedQuantity = orderEvent.path("quantity").asInt(-1);

                        if (productId == null || requestedQuantity < 0) {
                            throw new IllegalArgumentException("Order event must contain productId and quantity");
                        }

                        inventoryReserved = reserveInventory(availableStock, productId, requestedQuantity);

                        String inventoryEventJson = createInventoryEvent(record, productId, requestedQuantity,
                                inventoryReserved);
                        outbox.add(new InventoryStateStore.OutboxEvent(
                                "inventory-event-" + record.partition() + "-" + record.offset(), INVENTORY_EVENTS_TOPIC,
                                record.key(), inventoryEventJson, InventoryStateStore.OutboxEvent.Status.PENDING));
                        markProcessed(processedRecords, record);
                        stateStore.save(
                                new InventoryStateStore.InventoryState(availableStock, processedRecords, outbox));
                        inventoryConsumer.commitSync();
//                        throw new RuntimeException("Order event has been processed");
                        publishPendingEvents(inventoryProducer, outbox, stateStore, availableStock, processedRecords);
                    } catch (Exception exception) {
                        if (inventoryReserved) {
                            releaseInventory(availableStock, productId, requestedQuantity);
                        }
                        System.err.println("Failed to process order " + record.key());
                        exception.printStackTrace();
                        // Do not commit this batch.
                        break;
                    }
                }
            }
        }
    }

    private static void publishPendingEvents(KafkaProducer<String, String> producer,
            List<InventoryStateStore.OutboxEvent> outbox, InventoryStateStore stateStore,
            Map<String, Integer> availableStock, Set<String> processedRecords) throws Exception {
        for (int index = 0; index < outbox.size(); index++) {
            InventoryStateStore.OutboxEvent event = outbox.get(index);
            if (event.status() != InventoryStateStore.OutboxEvent.Status.PENDING) {
                continue;
            }

            RecordMetadata metadata = producer.send(new ProducerRecord<>(event.topic(), event.key(), event.value()))
                    .get();
            outbox.set(index,
                    new InventoryStateStore.OutboxEvent(event.eventId(), event.topic(), event.key(), event.value(),
                            InventoryStateStore.OutboxEvent.Status.PUBLISHED));
            stateStore.save(new InventoryStateStore.InventoryState(availableStock, processedRecords, outbox));
            System.out.printf("Inventory event published: topic=%s, partition=%d, offset=%d%n", metadata.topic(),
                    metadata.partition(), metadata.offset());
        }
    }

    static String recordIdentity(ConsumerRecord<String, String> record) {
        return record.topic() + "-" + record.partition() + "-" + record.offset();
    }

    static boolean isProcessed(Set<String> processedRecords, ConsumerRecord<String, String> record) {
        return processedRecords.contains(recordIdentity(record));
    }

    static void markProcessed(Set<String> processedRecords, ConsumerRecord<String, String> record) {
        processedRecords.add(recordIdentity(record));
    }

    static boolean reserveInventory(Map<String, Integer> availableStock, String productId, int requestedQuantity) {
        int currentQuantity = availableStock.getOrDefault(productId, 0);
        if (currentQuantity < requestedQuantity) {
            return false;
        }
        availableStock.put(productId, currentQuantity - requestedQuantity);
        return true;
    }

    static void releaseInventory(Map<String, Integer> availableStock, String productId, int requestedQuantity) {
        availableStock.merge(productId, requestedQuantity, Integer::sum);
    }

    static String createInventoryEvent(ConsumerRecord<String, String> record, String productId, int requestedQuantity,
            boolean inventoryReserved) {
        String eventType = inventoryReserved ? "InventoryReserved" : "InventoryRejected";

        return "{\"eventId\":\"inventory-event-" + record.partition() + "-" + record.offset() + "\","
                + "\"eventType\":\"" + eventType + "\"," + "\"eventVersion\":1," + "\"orderId\":\"" + record.key()
                + "\"," + "\"productId\":\"" + productId + "\"," + "\"quantity\":" + requestedQuantity + "}";
    }
}
