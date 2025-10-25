package com.backend.design.pattern.designs.ChessGame.ModularCode.chat;

import com.backend.design.pattern.designs.ChessGame.ModularCode.user.User;

/**
 * Mediator interface for handling communication between users.
 * Implements the Mediator Pattern to decouple user communication.
 */
public interface ChatMediator {

    /**
     * Sends a message from one user through the mediator.
     *
     * @param message the message to send
     * @param sender  the user sending the message
     */
    void sendMessage(Message message, User sender);

    /**
     * Adds a user to this mediator.
     *
     * @param user the user to add
     */
    void addUser(User user);

    /**
     * Removes a user from this mediator.
     *
     * @param user the user to remove
     */
    void removeUser(User user);
}

