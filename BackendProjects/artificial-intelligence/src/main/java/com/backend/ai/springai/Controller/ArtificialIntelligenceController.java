package com.backend.ai.springai.Controller;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtificialIntelligenceController {

/*
   ---------------- SETUP FOR ONE MODEL SUPPORT -----------------

    private final ChatClient _chatClient;

    ArtificialIntelligenceController(ChatClient.Builder builder) {
        this._chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("query") String query) {

        return _chatClient.prompt(query).call().content();
    }
*/


    // ---------------- SETUP FOR MULTIPLE MODEL SUPPORT -----------------
    private final ChatClient _claudeChatClient;
    private final ChatClient _ollamaChatClient;

    ArtificialIntelligenceController(AnthropicChatModel anthropicChatModel, OllamaChatModel _ollamaChatClient) {
        this._claudeChatClient = ChatClient.builder(anthropicChatModel).build();
        this._ollamaChatClient = ChatClient.builder(_ollamaChatClient).build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("query") String query) {
        if (query.equals("Hi")) {
            return _claudeChatClient.prompt(query).call().content();
        } else {
            return _ollamaChatClient.prompt(query).call().content();
        }
    }

}
