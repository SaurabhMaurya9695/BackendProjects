package com.backend.design.pattern.designs.ChessGame.ModularCode.model;

/**
 * Represents the result of a chess game.
 */
public enum GameResult {
    /** White player won */
    WHITE_WINS,

    /** Black player won */
    BLACK_WINS,

    /** Game ended in a draw */
    DRAW,

    /** Game is still ongoing */
    IN_PROGRESS
}

