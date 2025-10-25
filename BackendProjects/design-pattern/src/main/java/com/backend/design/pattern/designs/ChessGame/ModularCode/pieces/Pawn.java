package com.backend.design.pattern.designs.ChessGame.ModularCode.pieces;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pawn chess piece.
 * The Pawn has the most complex movement rules:
 * - Moves forward one square (or two from starting position)
 * - Captures diagonally forward one square
 * - Special moves: En passant and promotion (handled by chess rules)
 */
public class Pawn extends Piece {

    /**
     * Creates a new Pawn piece.
     *
     * @param color the color of the pawn
     */
    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public List<Position> getPossibleMoves(Position currentPos, Board board) {
        List<Position> moves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? -1 : 1;

        // Forward move (one square)
        Position oneStep = currentPos.offset(direction, 0);
        if (oneStep != null && !board.isOccupied(oneStep)) {
            moves.add(oneStep);

            // Double move from starting position
            if (!hasMoved) {
                Position twoStep = currentPos.offset(2 * direction, 0);
                if (twoStep != null && !board.isOccupied(twoStep)) {
                    moves.add(twoStep);
                }
            }
        }

        // Diagonal captures (left)
        Position leftCapture = currentPos.offset(direction, -1);
        if (leftCapture != null && board.isOccupied(leftCapture) &&
                !board.isOccupiedBySameColor(leftCapture, this.color)) {
            moves.add(leftCapture);
        }

        // Diagonal captures (right)
        Position rightCapture = currentPos.offset(direction, 1);
        if (rightCapture != null && board.isOccupied(rightCapture) &&
                !board.isOccupiedBySameColor(rightCapture, this.color)) {
            moves.add(rightCapture);
        }

        // Note: En passant is handled by SpecialMoveHandler in the rules package

        return moves;
    }

    /**
     * Checks if this pawn is on its starting rank.
     *
     * @param position the current position of the pawn
     * @return true if the pawn is on its starting rank
     */
    public boolean isOnStartingRank(Position position) {
        if (color == Color.WHITE) {
            return position.getRow() == 6;
        } else {
            return position.getRow() == 1;
        }
    }

    /**
     * Checks if this pawn has reached the promotion rank.
     *
     * @param position the current position of the pawn
     * @return true if the pawn has reached the opposite end of the board
     */
    public boolean canPromote(Position position) {
        if (color == Color.WHITE) {
            return position.getRow() == 0;
        } else {
            return position.getRow() == 7;
        }
    }

    @Override
    public Piece copy() {
        Pawn copy = new Pawn(this.color);
        if (this.hasMoved) {
            copy.setMoved();
        }
        return copy;
    }
}

