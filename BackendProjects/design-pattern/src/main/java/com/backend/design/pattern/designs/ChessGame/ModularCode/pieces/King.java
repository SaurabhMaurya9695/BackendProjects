package com.backend.design.pattern.designs.ChessGame.ModularCode.pieces;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a King chess piece.
 * The King can move one square in any direction (horizontal, vertical, or diagonal).
 * Special move: Castling (handled by chess rules).
 */
public class King extends Piece {

    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
    };

    /**
     * Creates a new King piece.
     *
     * @param color the color of the king
     */
    public King(Color color) {
        super(color, PieceType.KING);
    }

    @Override
    public List<Position> getPossibleMoves(Position currentPos, Board board) {
        List<Position> moves = new ArrayList<>();

        for (int[] dir : DIRECTIONS) {
            Position newPos = currentPos.offset(dir[0], dir[1]);
            if (newPos != null && !board.isOccupiedBySameColor(newPos, this.color)) {
                moves.add(newPos);
            }
        }

        return moves;
    }

    @Override
    public Piece copy() {
        King copy = new King(this.color);
        if (this.hasMoved) {
            copy.setMoved();
        }
        return copy;
    }
}

