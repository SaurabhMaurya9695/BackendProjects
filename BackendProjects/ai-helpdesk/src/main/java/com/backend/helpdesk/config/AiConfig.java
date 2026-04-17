package com.backend.helpdesk.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class AiConfig {

    @Value("classpath:prompt/ai-helpdesk-system.st")
    private Resource systemMessageResource;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) throws IOException {
        String systemMessage = new String(systemMessageResource.getContentAsByteArray(), StandardCharsets.UTF_8);
        return builder
                .defaultSystem(system -> system.text(systemMessage))
                .defaultAdvisors(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
