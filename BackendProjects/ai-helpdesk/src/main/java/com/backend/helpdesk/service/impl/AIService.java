package com.backend.helpdesk.service.impl;

import com.backend.helpdesk.tools.TicketDatabaseTool;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient _chatClient;
    private final TicketDatabaseTool _ticketDatabaseTool;

    public AIService(ChatClient chatClient, TicketDatabaseTool ticketDatabaseTool) {
        _chatClient = chatClient;
        _ticketDatabaseTool = ticketDatabaseTool;
    }

    public String getResponseFromAssistance(String query) {
        return this._chatClient
                .prompt(query)
                .tools(_ticketDatabaseTool)
                .call()
                .content();
    }
}
