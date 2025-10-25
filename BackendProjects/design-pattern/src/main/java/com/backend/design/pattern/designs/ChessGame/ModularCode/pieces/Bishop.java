package com.backend.design.pattern.designs.ChessGame.ModularCode.pieces;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bishop chess piece.
 * The Bishop can move any number of squares diagonally.
 */
public class Bishop extends Piece {

    private static final int[][] DIRECTIONS = {
            {-1, -1},  // Up-Left
            {-1, 1},   // Up-Right
            {1, -1},   // Down-Left
            {1, 1}     // Down-Right
    };

    /**
     * Creates a new Bishop piece.
     *
     * @param color the color of the bishop
     */
    public Bishop(Color color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public List<Position> getPossibleMoves(Position currentPos, Board board) {
        List<Position> moves = new ArrayList<>();

        for (int[] dir : DIRECTIONS) {
            for (int step = 1; step < 8; step++) {
                Position newPos = currentPos.offset(dir[0] * step, dir[1] * step);

                if (newPos == null) {
                    break; // Out of bounds
                }

                if (board.isOccupiedBySameColor(newPos, this.color)) {
                    break; // Blocked by own piece
                }

                moves.add(newPos);

                if (board.isOccupied(newPos)) {
                    break; // Can capture, but can't move further
                }
            }
        }

        return moves;
    }

    @Override
    public Piece copy() {
        Bishop copy = new Bishop(this.color);
        if (this.hasMoved) {
            copy.setMoved();
        }
        return copy;
    }
}

