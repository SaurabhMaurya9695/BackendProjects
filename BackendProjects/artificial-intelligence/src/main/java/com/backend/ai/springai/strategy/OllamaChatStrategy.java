package com.backend.ai.springai.strategy;

import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation for Ollama (local LLM).
 * Handles all chat interactions routed to the local Ollama model.
 */
@Component
public class OllamaChatStrategy implements ChatModelStrategy {

    private final ChatClient chatClient;

    public OllamaChatStrategy(OllamaChatModel ollamaChatModel) {
        this.chatClient = ChatClient.builder(ollamaChatModel).build();
    }

    @Override
    public String chat(String query) {
        return chatClient.prompt(query).call().content();
    }

    @Override
    public String getModelName() {
        return "ollama";
    }

    @Override
    public Flux<String> streamChat(String query) {
        return chatClient.prompt(query).stream().content();
    }
}
