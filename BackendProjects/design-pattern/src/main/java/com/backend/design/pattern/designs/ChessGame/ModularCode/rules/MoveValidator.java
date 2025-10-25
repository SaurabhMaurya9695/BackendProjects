package com.backend.design.pattern.designs.ChessGame.ModularCode.rules;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;

import java.util.List;

/**
 * Validates chess moves according to the rules.
 * Separates validation logic following Single Responsibility Principle.
 */
public class MoveValidator {

    private final ChessRules chessRules;
    private final SpecialMoveHandler specialMoveHandler;

    /**
     * Creates a new MoveValidator.
     *
     * @param chessRules the chess rules implementation
     */
    public MoveValidator(ChessRules chessRules) {
        this.chessRules = chessRules;
        this.specialMoveHandler = new SpecialMoveHandler();
    }

    /**
     * Validates if a move is legal.
     *
     * @param move  the move to validate
     * @param board the current board state
     * @return true if the move is valid
     */
    public boolean validate(Move move, Board board) {
        Piece piece = move.getPiece();

        // Check if the move is in the piece's possible moves
        if (!isPossibleMove(move, board)) {
            return false;
        }

        // Check special moves
        if (move.isCastling() || move.isEnPassant() || move.isPromotion()) {
            if (!specialMoveHandler.validateSpecialMove(move, board)) {
                return false;
            }
        }

        // Check if move would put own king in check
        return !chessRules.wouldMoveCauseCheck(move, board, piece.getColor());
    }

    /**
     * Checks if the destination is in the piece's possible moves.
     *
     * @param move  the move to check
     * @param board the current board state
     * @return true if the destination is reachable
     */
    private boolean isPossibleMove(Move move, Board board) {
        Piece piece = move.getPiece();
        List<Position> possibleMoves = piece.getPossibleMoves(move.getFrom(), board);

        for (Position pos : possibleMoves) {
            if (pos.equals(move.getTo())) {
                return true;
            }
        }

        return false;
    }
}

