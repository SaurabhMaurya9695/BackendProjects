package com.backend.ai.springai.Controller;

import reactor.core.publisher.Flux;
import com.backend.ai.springai.service.ChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtificialIntelligenceController {

    private final ChatService chatService;

    ArtificialIntelligenceController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * @param query          the prompt to send to the model
     * @param model          optional — "claude" or "ollama" (defaults to "ollama")
     * @param conversationId pass the same ID across requests to maintain chat memory
     */
    @GetMapping("/chat")
    public String chat(@RequestParam("query") String query,
            @RequestParam(value = "model", defaultValue = "ollama") String model,
            @RequestParam(value = "conversationId", defaultValue = "defalt") String conversationId) {
        return chatService.chat(query, model, conversationId);
    }

    @GetMapping("/stream-chat")
    public ResponseEntity<Flux<String>> streamChat(@RequestParam("query") String query,
            @RequestParam(value = "model", defaultValue = "ollama") String model,
            @RequestParam(value = "conversationId", defaultValue = "default") String conversationId) {
        return ResponseEntity.ok(chatService.streamChat(query, model, conversationId));
    }
}
