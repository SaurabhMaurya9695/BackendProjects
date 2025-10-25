package com.backend.design.pattern.designs.ChessGame.ModularCode.exception;

/**
 * Thrown when attempting to perform an action on a game that is not in progress.
 */
public class GameNotInProgressException extends ChessException {

    public GameNotInProgressException(String message) {
        super(message);
    }
}

