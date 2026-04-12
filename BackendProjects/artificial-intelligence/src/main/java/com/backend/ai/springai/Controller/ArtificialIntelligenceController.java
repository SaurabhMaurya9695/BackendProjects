package com.backend.ai.springai.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtificialIntelligenceController {

    private final ChatClient _chatClient;

    ArtificialIntelligenceController(ChatClient.Builder builder) {
        this._chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("query") String query) {

        return _chatClient.prompt(query).call().content();
    }
}
