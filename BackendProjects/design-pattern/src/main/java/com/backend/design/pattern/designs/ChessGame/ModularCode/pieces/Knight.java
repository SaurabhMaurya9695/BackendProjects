package com.backend.design.pattern.designs.ChessGame.ModularCode.pieces;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Knight chess piece.
 * The Knight moves in an L-shape: two squares in one direction and one square perpendicular.
 * The Knight is the only piece that can jump over other pieces.
 */
public class Knight extends Piece {

    private static final int[][] KNIGHT_MOVES = {
            {-2, -1}, {-2, 1},  // Two up, one left/right
            {-1, -2}, {-1, 2},  // One up, two left/right
            {1, -2},  {1, 2},   // One down, two left/right
            {2, -1},  {2, 1}    // Two down, one left/right
    };

    /**
     * Creates a new Knight piece.
     *
     * @param color the color of the knight
     */
    public Knight(Color color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    public List<Position> getPossibleMoves(Position currentPos, Board board) {
        List<Position> moves = new ArrayList<>();

        for (int[] move : KNIGHT_MOVES) {
            Position newPos = currentPos.offset(move[0], move[1]);
            if (newPos != null && !board.isOccupiedBySameColor(newPos, this.color)) {
                moves.add(newPos);
            }
        }

        return moves;
    }

    @Override
    public Piece copy() {
        Knight copy = new Knight(this.color);
        if (this.hasMoved) {
            copy.setMoved();
        }
        return copy;
    }
}

