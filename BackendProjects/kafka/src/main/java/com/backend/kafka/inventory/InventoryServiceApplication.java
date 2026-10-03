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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

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

        try (KafkaConsumer<String, String> inventoryConsumer = new KafkaConsumer<>(
                KafkaClientProperties.consumerProperties(CONSUMER_GROUP));
                KafkaProducer<String, String> inventoryProducer = new KafkaProducer<>(
                        KafkaClientProperties.producerProperties())) {
            inventoryConsumer.subscribe(Collections.singletonList(ORDERS_TOPIC));
            while (true) {
                // 1 : Poll the records from Order Topic
                ConsumerRecords<String, String> records = inventoryConsumer.poll(Duration.ofMillis(500));
                // 2 : Iterate through all records
                boolean outputPublished = true;
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
                    boolean outputPublishedForRecord = false;
                    try {
                        // 3.1 - get the full json here
                        JsonNode orderEvent = objectMapper.readTree(record.value());
                        productId = orderEvent.path("productId").asText(null);
                        requestedQuantity = orderEvent.path("quantity").asInt(-1);

                        if (productId == null || requestedQuantity < 0) {
                            throw new IllegalArgumentException("Order event must contain productId and quantity");
                        }

                        inventoryReserved = reserveInventory(availableStock, productId, requestedQuantity);

                        // 4 : Created a producer record
                        String inventoryEventJson = createInventoryEvent(record, productId, requestedQuantity,
                                inventoryReserved);
                        ProducerRecord<String, String> inventoryEventRecord = new ProducerRecord<>(
                                INVENTORY_EVENTS_TOPIC, record.key(), inventoryEventJson);

                        // 5 : Send the event and wait for Kafka's result
                        Future<RecordMetadata> sendResult = inventoryProducer.send(inventoryEventRecord);
                        RecordMetadata metadata = sendResult.get();
                        outputPublishedForRecord = true;
                        markProcessed(processedRecords, record);
                        stateStore.save(new InventoryStateStore.InventoryState(availableStock, processedRecords));
                        System.out.printf("Inventory event published: topic=%s, partition=%d, offset=%d%n",
                                metadata.topic(), metadata.partition(), metadata.offset());
                    } catch (Exception exception) {
                        outputPublished = false;
                        if (inventoryReserved && !outputPublishedForRecord) {
                            releaseInventory(availableStock, productId, requestedQuantity);
                        }
                        System.err.println("Failed to process order " + record.key());
                        exception.printStackTrace();
                        // Do not commit this batch.
                        break;
                    }

                }

                // 6 : with a successful transaction with all 1 - 5 we'll commit this msg
                if (!records.isEmpty() && outputPublished) {
                    inventoryConsumer.commitSync();
                    System.out.println("Offsets committed");
                }
            }
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

    static boolean reserveInventory(Map<String, Integer> availableStock, String productId,
            int requestedQuantity) {
        int currentQuantity = availableStock.getOrDefault(productId, 0);
        if (currentQuantity < requestedQuantity) {
            return false;
        }
        availableStock.put(productId, currentQuantity - requestedQuantity);
        return true;
    }

    static void releaseInventory(Map<String, Integer> availableStock, String productId,
            int requestedQuantity) {
        availableStock.merge(productId, requestedQuantity, Integer::sum);
    }

    static String createInventoryEvent(ConsumerRecord<String, String> record, String productId,
            int requestedQuantity, boolean inventoryReserved) {
        String eventType = inventoryReserved ? "InventoryReserved" : "InventoryRejected";

        return "{\"eventId\":\"inventory-event-" + record.partition() + "-" + record.offset() + "\","
                + "\"eventType\":\"" + eventType + "\"," + "\"eventVersion\":1," + "\"orderId\":\"" + record.key()
                + "\"," + "\"productId\":\"" + productId + "\"," + "\"quantity\":" + requestedQuantity + "}";
    }
}
