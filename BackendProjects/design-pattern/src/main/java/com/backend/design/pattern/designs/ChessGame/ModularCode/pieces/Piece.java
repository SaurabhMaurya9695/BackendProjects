package com.backend.design.pattern.designs.ChessGame.ModularCode.pieces;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;

import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all chess pieces.
 * Implements the Strategy Pattern - each piece type has its own movement strategy.
 * This class is immutable except for the hasMoved flag which tracks piece movement.
 */
public abstract class Piece {

    protected final Color color;
    protected final PieceType type;
    protected boolean hasMoved;

    /**
     * Creates a new chess piece.
     *
     * @param color the color of the piece (WHITE or BLACK)
     * @param type  the type of the piece
     */
    protected Piece(Color color, PieceType type) {
        this.color = Objects.requireNonNull(color, "Color cannot be null");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.hasMoved = false;
    }

    /**
     * Returns the color of this piece.
     *
     * @return the piece color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the type of this piece.
     *
     * @return the piece type
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Checks if this piece has moved from its starting position.
     *
     * @return true if the piece has moved
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Marks this piece as having moved.
     * Used for tracking castling eligibility and pawn double-move.
     */
    public void setMoved() {
        this.hasMoved = true;
    }

    /**
     * Calculates all possible moves for this piece from the current position.
     * This includes pseudo-legal moves (moves that don't check for king safety).
     *
     * @param currentPos the current position of the piece
     * @param board      the current board state
     * @return list of possible destination positions
     */
    public abstract List<Position> getPossibleMoves(Position currentPos, Board board);

    /**
     * Returns the algebraic notation symbol for this piece.
     *
     * @return the piece symbol (K, Q, R, B, N, P)
     */
    public String getSymbol() {
        return type.getSymbol();
    }

    /**
     * Returns a string representation of this piece including its color.
     *
     * @return string like "WK" for white king, "BQ" for black queen
     */
    @Override
    public String toString() {
        String colorStr = (color == Color.WHITE) ? "W" : "B";
        return colorStr + getSymbol();
    }

    /**
     * Creates a copy of this piece with the same properties.
     *
     * @return a new piece instance with same color, type, and hasMoved status
     */
    public abstract Piece copy();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece piece = (Piece) obj;
        return hasMoved == piece.hasMoved &&
                color == piece.color &&
                type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type, hasMoved);
    }
}

