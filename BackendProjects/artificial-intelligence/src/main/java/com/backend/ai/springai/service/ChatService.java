package com.backend.ai.springai.service;

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

    public String chat(String query, String model) {
        return chatModelFactory.getStrategy(model).chat(query);
    }
}
