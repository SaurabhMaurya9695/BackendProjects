package com.backend.design.pattern.designs.ChessGame.ModularCode.exception;

/**
 * Thrown when an invalid chess move is attempted.
 */
public class InvalidMoveException extends ChessException {

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}

