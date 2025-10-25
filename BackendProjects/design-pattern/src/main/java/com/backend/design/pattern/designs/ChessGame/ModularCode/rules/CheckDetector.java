package com.backend.design.pattern.designs.ChessGame.ModularCode.rules;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;

import java.util.List;

/**
 * Detects check conditions on the chess board.
 * Separated into its own class following Single Responsibility Principle.
 */
public class CheckDetector {

    /**
     * Checks if the specified color's king is in check.
     *
     * @param color the color to check
     * @param board the current board state
     * @return true if the king is under attack
     */
    public boolean isInCheck(Color color, Board board) {
        Position kingPos = board.findKing(color);
        if (kingPos == null) {
            return false; // King not found (shouldn't happen in valid game)
        }

        Color opponentColor = color.opposite();
        List<Position> opponentPieces = board.getAllPiecesOfColor(opponentColor);

        // Check if any opponent piece can attack the king
        for (Position pos : opponentPieces) {
            Piece piece = board.getPiece(pos);
            List<Position> attackMoves = piece.getPossibleMoves(pos, board);

            for (Position attackPos : attackMoves) {
                if (attackPos.equals(kingPos)) {
                    return true; // King is under attack
                }
            }
        }

        return false;
    }

    /**
     * Checks if a specific position is under attack by the specified color.
     *
     * @param position the position to check
     * @param byColor  the attacking color
     * @param board    the current board state
     * @return true if the position is under attack
     */
    public boolean isPositionUnderAttack(Position position, Color byColor, Board board) {
        List<Position> attackingPieces = board.getAllPiecesOfColor(byColor);

        for (Position pos : attackingPieces) {
            Piece piece = board.getPiece(pos);
            List<Position> attackMoves = piece.getPossibleMoves(pos, board);

            for (Position attackPos : attackMoves) {
                if (attackPos.equals(position)) {
                    return true;
                }
            }
        }

        return false;
    }
}

