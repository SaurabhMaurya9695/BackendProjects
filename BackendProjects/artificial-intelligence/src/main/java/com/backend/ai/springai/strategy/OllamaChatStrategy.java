package com.backend.ai.springai.strategy;

import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Strategy implementation for Ollama (local LLM).
 * Handles all chat interactions routed to the local Ollama model.
 */
@Component
public class OllamaChatStrategy implements ChatModelStrategy {

    private final ChatClient chatClient;
    private final MessageChatMemoryAdvisor memoryAdvisor;

    @Autowired
    private VectorStore _vectorStore;

    public OllamaChatStrategy(OllamaChatModel ollamaChatModel, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        this.chatClient = ChatClient.builder(ollamaChatModel).build();
        this.memoryAdvisor = MessageChatMemoryAdvisor.builder(
                MessageWindowChatMemory.builder().chatMemoryRepository(jdbcChatMemoryRepository).build()).build();
    }

    @Override
    public String chat(String query, String conversationId) {
        return chatClient
                .prompt(query)
                .advisors(memoryAdvisor)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    @Override
    public String getModelName() {
        return "ollama";
    }

    @Override
    public Flux<String> streamChat(String query, String conversationId) {
        return chatClient
                .prompt(query)
                .advisors(memoryAdvisor)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    public void saveToDB(List<String> lst){
        List<Document> list = lst.stream().map(Document::new).toList();
        this._vectorStore.add(list);
    }



}
