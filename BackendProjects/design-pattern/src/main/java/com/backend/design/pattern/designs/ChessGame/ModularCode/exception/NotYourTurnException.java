package com.backend.design.pattern.designs.ChessGame.ModularCode.exception;

/**
 * Thrown when a player attempts to move when it's not their turn.
 */
public class NotYourTurnException extends ChessException {

    public NotYourTurnException(String message) {
        super(message);
    }
}

