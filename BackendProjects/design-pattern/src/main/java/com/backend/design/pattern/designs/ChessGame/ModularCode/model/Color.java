package com.backend.design.pattern.designs.ChessGame.ModularCode.model;

/**
 * Represents the color of chess pieces and players.
 * Used to distinguish between white and black sides in a chess game.
 */
public enum Color {
    WHITE,
    BLACK;

    /**
     * Returns the opposite color.
     *
     * @return BLACK if this is WHITE, WHITE if this is BLACK
     */
    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}

