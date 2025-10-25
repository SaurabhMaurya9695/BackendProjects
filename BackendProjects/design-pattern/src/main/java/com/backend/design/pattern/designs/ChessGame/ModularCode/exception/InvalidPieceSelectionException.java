package com.backend.design.pattern.designs.ChessGame.ModularCode.exception;

/**
 * Thrown when attempting to select an invalid piece.
 */
public class InvalidPieceSelectionException extends ChessException {

    public InvalidPieceSelectionException(String message) {
        super(message);
    }
}

