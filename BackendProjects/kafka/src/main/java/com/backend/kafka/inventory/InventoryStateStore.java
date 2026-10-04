package com.backend.kafka.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryStateStore {

    private final Path stateFile;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InventoryStateStore(Path stateFile) {
        this.stateFile = Objects.requireNonNull(stateFile, "stateFile must not be null");
    }

    public InventoryState load() {
        if (Files.notExists(stateFile)) {
            return new InventoryState(Map.of("product-1", 10));
        }
        try {
            return objectMapper.readValue(stateFile.toFile(), InventoryState.class);
        } catch (IOException exception) {
            throw new InventoryStateException("Failed to load inventory state from " + stateFile, exception);
        }
    }

    public void save(InventoryState state) {
        Path file = stateFile.toAbsolutePath();
        Path temporaryFile = null;
        try {
            Files.createDirectories(file.getParent());
            temporaryFile = Files.createTempFile(file.getParent(), file.getFileName().toString(), ".tmp");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(temporaryFile.toFile(), state);
            try {
                Files.move(temporaryFile, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            throw new InventoryStateException("Failed to save inventory state to " + stateFile, exception);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // The state file has already been replaced.
                }
            }
        }
    }

    public record InventoryState(Map<String, Integer> availableStock) {

        public InventoryState {
            availableStock = availableStock == null ? new HashMap<>() : new HashMap<>(availableStock);
        }
    }

    public static class InventoryStateException extends RuntimeException {

        public InventoryStateException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
