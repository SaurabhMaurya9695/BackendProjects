package com.backend.design.pattern.designs.ChessGame.ModularCode.pieces;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Queen chess piece.
 * The Queen can move any number of squares along a rank, file, or diagonal.
 * It combines the power of a Rook and Bishop.
 */
public class Queen extends Piece {

    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
    };

    /**
     * Creates a new Queen piece.
     *
     * @param color the color of the queen
     */
    public Queen(Color color) {
        super(color, PieceType.QUEEN);
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
        Queen copy = new Queen(this.color);
        if (this.hasMoved) {
            copy.setMoved();
        }
        return copy;
    }
}

