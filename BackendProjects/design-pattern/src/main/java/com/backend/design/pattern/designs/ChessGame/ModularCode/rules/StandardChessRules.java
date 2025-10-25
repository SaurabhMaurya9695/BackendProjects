package com.backend.design.pattern.designs.ChessGame.ModularCode.rules;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;

import java.util.List;

/**
 * Standard implementation of chess rules.
 * Handles move validation, check detection, and game-ending conditions.
 */
public class StandardChessRules implements ChessRules {

    private final MoveValidator moveValidator;
    private final CheckDetector checkDetector;

    /**
     * Creates a new StandardChessRules with default validators.
     */
    public StandardChessRules() {
        this.moveValidator = new MoveValidator(this);
        this.checkDetector = new CheckDetector();
    }

    @Override
    public boolean isValidMove(Move move, Board board) {
        return moveValidator.validate(move, board);
    }

    @Override
    public boolean isInCheck(Color color, Board board) {
        return checkDetector.isInCheck(color, board);
    }

    @Override
    public boolean wouldMoveCauseCheck(Move move, Board board, Color kingColor) {
        // Create a temporary board to test the move
        Board testBoard = board.copy();

        // Execute the move on the test board
        testBoard.movePiece(move.getFrom(), move.getTo());

        // Check if king is in check after the move
        return isInCheck(kingColor, testBoard);
    }

    @Override
    public boolean isCheckmate(Color color, Board board) {
        // Must be in check for checkmate
        if (!isInCheck(color, board)) {
            return false;
        }

        // Check if any legal move exists
        return !hasAnyLegalMove(color, board);
    }

    @Override
    public boolean isStalemate(Color color, Board board) {
        // Must NOT be in check for stalemate
        if (isInCheck(color, board)) {
            return false;
        }

        // Check if any legal move exists
        return !hasAnyLegalMove(color, board);
    }

    /**
     * Checks if the specified color has any legal move available.
     *
     * @param color the color to check
     * @param board the current board state
     * @return true if at least one legal move exists
     */
    private boolean hasAnyLegalMove(Color color, Board board) {
        List<Position> pieces = board.getAllPiecesOfColor(color);

        for (Position pos : pieces) {
            Piece piece = board.getPiece(pos);
            List<Position> possibleMoves = piece.getPossibleMoves(pos, board);

            for (Position targetPos : possibleMoves) {
                Move move = new Move(pos, targetPos, piece, board.getPiece(targetPos));
                if (isValidMove(move, board)) {
                    return true; // Found at least one legal move
                }
            }
        }

        return false; // No legal moves found
    }
}

