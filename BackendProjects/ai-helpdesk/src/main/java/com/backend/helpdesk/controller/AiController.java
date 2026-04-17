package com.backend.helpdesk.controller;

import com.backend.helpdesk.service.impl.AIService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AIService _aiService;

    public AiController(AIService aiService) {
        _aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<String> getResponse(@RequestBody String query) {
        return ResponseEntity.ok(_aiService.getResponseFromAssistance(query));
    }
}
