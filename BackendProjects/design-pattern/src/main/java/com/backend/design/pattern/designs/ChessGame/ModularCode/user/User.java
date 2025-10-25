package com.backend.design.pattern.designs.ChessGame.ModularCode.user;

import com.backend.design.pattern.designs.ChessGame.ModularCode.chat.Colleague;
import com.backend.design.pattern.designs.ChessGame.ModularCode.chat.Message;

import java.util.Objects;

/**
 * Represents a chess player/user in the system.
 * Extends Colleague to participate in the chat mediator pattern.
 */
public class User extends Colleague {

    private final String id;
    private final String name;
    private int score;

    /**
     * Creates a new user with a default starting score.
     *
     * @param id   unique user identifier
     * @param name user's display name
     */
    public User(String id, String name) {
        this(id, name, 1000); // Default starting score
    }

    /**
     * Creates a new user with specified score.
     *
     * @param id    unique user identifier
     * @param name  user's display name
     * @param score initial score
     */
    public User(String id, String name, int score) {
        this.id = Objects.requireNonNull(id, "User ID cannot be null");
        this.name = Objects.requireNonNull(name, "User name cannot be null");
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    /**
     * Increases the user's score.
     *
     * @param points points to add
     */
    public void incrementScore(int points) {
        this.score += points;
    }

    /**
     * Decreases the user's score.
     *
     * @param points points to subtract
     */
    public void decrementScore(int points) {
        this.score -= points;
    }

    @Override
    public void send(Message message) {
        if (mediator != null) {
            mediator.sendMessage(message, this);
        }
    }

    @Override
    public void receive(Message message) {
        System.out.println("[" + name + "] received: " + message.getContent() +
                " from " + message.getSenderId());
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ", Score: " + score + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

