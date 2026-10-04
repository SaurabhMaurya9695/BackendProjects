package com.backend.kafka.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class InventoryStateStore {

    private static final String INITIAL_PRODUCT_ID = "product-1";
    private static final int INITIAL_PRODUCT_QUANTITY = 10;

    private final Path stateFile;
    private final ObjectMapper objectMapper;

    public InventoryStateStore(Path stateFile) {
        this.stateFile = Objects.requireNonNull(stateFile, "stateFile must not be null");
        this.objectMapper = new ObjectMapper();
    }

    public InventoryState load() {
        if (Files.notExists(stateFile)) {
            return initialState();
        }

        try {
            return objectMapper.readValue(stateFile.toFile(), InventoryState.class);
        } catch (IOException exception) {
            throw new InventoryStateException("Failed to load inventory state from " + stateFile, exception);
        }
    }

    public void save(InventoryState state) {
        Objects.requireNonNull(state, "state must not be null");

        Path absoluteStateFile = stateFile.toAbsolutePath();
        Path parentDirectory = absoluteStateFile.getParent();
        Path temporaryFile = null;

        try {

            Files.createDirectories(parentDirectory);
            temporaryFile = Files.createTempFile(parentDirectory, stateFile.getFileName().toString(), ".tmp");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(temporaryFile.toFile(), state);
            moveIntoPlace(temporaryFile, absoluteStateFile);
        } catch (IOException exception) {
            throw new InventoryStateException("Failed to save inventory state to " + stateFile, exception);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // The temporary file is harmless if the atomic move already succeeded.
                }
            }
        }
    }

    static InventoryState initialState() {
        Map<String, Integer> availableStock = new HashMap<>();
        availableStock.put(INITIAL_PRODUCT_ID, INITIAL_PRODUCT_QUANTITY);
        return new InventoryState(availableStock, new HashSet<>(), new ArrayList<>());
    }

    private static void moveIntoPlace(Path temporaryFile, Path stateFile) throws IOException {
        try {
            Files.move(temporaryFile, stateFile, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, stateFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public record InventoryState(Map<String, Integer> availableStock, Set<String> processedRecords,
            List<OutboxEvent> outbox) {

        public InventoryState {
            availableStock = availableStock == null ? new HashMap<>() : new HashMap<>(availableStock);
            processedRecords = processedRecords == null ? new HashSet<>() : new HashSet<>(processedRecords);
            outbox = outbox == null ? new ArrayList<>() : new ArrayList<>(outbox);
        }
    }

    public record OutboxEvent(String eventId, String topic, String key, String value, Status status) {

        public enum Status {
            PENDING,
            PUBLISHED
        }
    }

    public static class InventoryStateException extends RuntimeException {

        public InventoryStateException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
