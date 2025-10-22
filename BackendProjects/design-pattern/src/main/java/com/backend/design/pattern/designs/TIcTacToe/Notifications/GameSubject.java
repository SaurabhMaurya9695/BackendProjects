package com.backend.design.pattern.designs.TIcTacToe.Notifications;

/**
 * Subject interface for Observable pattern
 * Manages observers and notifications
 */
public interface GameSubject {

    /**
     * Attach an observer to receive notifications
     */
    void attach(GameObserver observer);

    /**
     * Detach an observer from receiving notifications
     */
    void detach(GameObserver observer);

    /**
     * Notify all attached observers
     */
    void notifyObservers();
}

