package com.backend.design.pattern.designs.Tinder.Models;

import java.util.ArrayList;
import java.util.List;

public class ChatRooms {

    private String chatRoomId;
    private List<String> participants;
    private List<Message> _messages;

    public ChatRooms(String chatRoomId, String userId1, String userId2) {
        this.chatRoomId = chatRoomId;
        participants = new ArrayList<>();
        _messages = new ArrayList<>();
        participants.add(userId1);
        participants.add(userId2);
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public boolean hasParticipant(String userId) {
        return participants.contains(userId);
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return _messages;
    }

    public void setMessages(List<Message> messages) {
        _messages = messages;
    }

    public void addMessage(String senderId, String content) {
        Message message = new Message(senderId, content);
        _messages.add(message);
    }

    public void displayChat() {
        System.out.println("=== Chat Room Started : " + chatRoomId + " ===");
        for (Message message : getMessages()) {
            System.out.println(
                    "[ " + message.getFormattedTime() + "] " + message.getSenderId() + ": " + message.getContent());
        }
        System.out.println("=== Chat Room End === ");
    }
}
