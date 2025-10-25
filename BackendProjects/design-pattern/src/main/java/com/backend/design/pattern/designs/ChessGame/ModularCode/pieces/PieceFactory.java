package com.backend.design.pattern.designs.ChessGame.ModularCode.pieces;

import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;

/**
 * Factory class for creating chess pieces.
 * Implements the Factory Pattern to encapsulate piece creation logic.
 * This provides a single point of piece instantiation, making the system more maintainable.
 */
public class PieceFactory {

    /**
     * Creates a new chess piece of the specified type and color.
     *
     * @param type  the type of piece to create
     * @param color the color of the piece
     * @return a new piece instance
     * @throws IllegalArgumentException if an unknown piece type is provided
     */
    public static Piece createPiece(PieceType type, Color color) {
        switch (type) {
            case KING:
                return new King(color);
            case QUEEN:
                return new Queen(color);
            case ROOK:
                return new Rook(color);
            case BISHOP:
                return new Bishop(color);
            case KNIGHT:
                return new Knight(color);
            case PAWN:
                return new Pawn(color);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }

    /**
     * Creates a copy of the given piece.
     *
     * @param piece the piece to copy
     * @return a new piece with the same properties
     */
    public static Piece copyPiece(Piece piece) {
        return piece.copy();
    }
}

