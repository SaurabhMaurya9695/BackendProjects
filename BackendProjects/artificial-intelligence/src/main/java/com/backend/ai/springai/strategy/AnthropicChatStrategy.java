package com.backend.ai.springai.strategy;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation for Anthropic Claude.
 * Handles all chat interactions routed to the Claude model.
 */
@Component
public class AnthropicChatStrategy implements ChatModelStrategy {

    private final ChatClient chatClient;

    public AnthropicChatStrategy(AnthropicChatModel anthropicChatModel) {
        this.chatClient = ChatClient.builder(anthropicChatModel).build();
    }

    @Override
    public String chat(String query) {
        return chatClient.prompt(query).call().content();
    }

    @Override
    public String getModelName() {
        return "claude";
    }
}
