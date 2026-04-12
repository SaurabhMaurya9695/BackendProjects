package com.backend.ai.springai.factory;

import com.backend.ai.springai.strategy.ChatModelStrategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory Pattern — resolves the correct ChatModelStrategy by model name.
 * <p>
 * All strategies are auto-discovered via Spring DI (List injection).
 * Adding a new model = add a new @Component strategy, nothing else changes.
 */
@Component
public class ChatModelFactory {

    private final Map<String, ChatModelStrategy> strategies;

    public ChatModelFactory(List<ChatModelStrategy> strategies) {
        this.strategies = strategies.stream().collect(
                Collectors.toMap(ChatModelStrategy::getModelName, Function.identity()));
    }

    /**
     * Returns the strategy for the given model name.
     * Falls back to "ollama" (local/free) if model is not specified or unknown.
     */
    public ChatModelStrategy getStrategy(String model) {
        return strategies.getOrDefault(model, strategies.get("ollama"));
    }
}
