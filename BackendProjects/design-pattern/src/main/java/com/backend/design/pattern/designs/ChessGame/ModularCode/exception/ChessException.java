package com.backend.design.pattern.designs.ChessGame.ModularCode.exception;

/**
 * Base exception for all chess game related errors.
 * Follows the exception hierarchy pattern for better error handling.
 */
public class ChessException extends Exception {

    public ChessException(String message) {
        super(message);
    }

    public ChessException(String message, Throwable cause) {
        super(message, cause);
    }
}

