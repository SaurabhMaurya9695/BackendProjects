package com.backend.ai.springai.service;

import reactor.core.publisher.Flux;
import com.backend.ai.springai.factory.ChatModelFactory;

import org.springframework.stereotype.Service;

/**
 * Service layer — owns the routing logic.
 * Controller stays thin; all model selection happens here.
 */
@Service
public class ChatService {

    private final ChatModelFactory chatModelFactory;

    public ChatService(ChatModelFactory chatModelFactory) {
        this.chatModelFactory = chatModelFactory;
    }

    public String chat(String query, String model, String conversationId) {
        return chatModelFactory.getStrategy(model).chat(query, conversationId);
    }

    public Flux<String> streamChat(String query, String model, String conversationId) {
        return chatModelFactory.getStrategy(model).streamChat(query, conversationId);
    }
}
