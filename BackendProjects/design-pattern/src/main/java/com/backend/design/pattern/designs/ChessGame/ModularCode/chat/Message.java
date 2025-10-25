package com.backend.design.pattern.designs.ChessGame.ModularCode.chat;

import java.util.Objects;

/**
 * Immutable message object for chat communication.
 * Contains sender information, content, and timestamp.
 */
public final class Message {

    private final String senderId;
    private final String content;
    private final long timestamp;

    /**
     * Creates a new message.
     *
     * @param senderId the ID of the sender
     * @param content  the message content
     */
    public Message(String senderId, String content) {
        this.senderId = Objects.requireNonNull(senderId, "Sender ID cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.timestamp = System.currentTimeMillis();
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + senderId + "]: " + content;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Message message = (Message) obj;
        return timestamp == message.timestamp &&
                Objects.equals(senderId, message.senderId) &&
                Objects.equals(content, message.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, content, timestamp);
    }
}

