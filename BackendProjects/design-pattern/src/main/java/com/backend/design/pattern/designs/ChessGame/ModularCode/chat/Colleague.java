package com.backend.design.pattern.designs.ChessGame.ModularCode.chat;

/**
 * Abstract colleague class for the Mediator Pattern.
 * Users extend this to participate in mediated communication.
 */
public abstract class Colleague {

    protected ChatMediator mediator;

    /**
     * Sends a message through the mediator.
     *
     * @param message the message to send
     */
    public abstract void send(Message message);

    /**
     * Receives a message from the mediator.
     *
     * @param message the received message
     */
    public abstract void receive(Message message);

    /**
     * Sets the mediator for this colleague.
     *
     * @param mediator the mediator to use
     */
    public void setMediator(ChatMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Gets the current mediator.
     *
     * @return the mediator
     */
    public ChatMediator getMediator() {
        return mediator;
    }
}

