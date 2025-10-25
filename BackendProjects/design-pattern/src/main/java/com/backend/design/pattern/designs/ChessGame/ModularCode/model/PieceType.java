package com.backend.design.pattern.designs.ChessGame.ModularCode.model;

/**
 * Represents the different types of chess pieces.
 */
public enum PieceType {
    KING("K"),
    QUEEN("Q"),
    ROOK("R"),
    BISHOP("B"),
    KNIGHT("N"),
    PAWN("P");

    private final String symbol;

    PieceType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the standard algebraic notation symbol for this piece type.
     *
     * @return the symbol (e.g., "K" for King, "Q" for Queen)
     */
    public String getSymbol() {
        return symbol;
    }
}

