package com.backend.design.pattern.designs.ChessGame.ModularCode.rules;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.MoveType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Pawn;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;

/**
 * Handles validation and execution of special chess moves.
 * Special moves include: Castling, En Passant, and Pawn Promotion.
 * Follows Single Responsibility Principle by separating special move logic.
 */
public class SpecialMoveHandler {

    /**
     * Validates a special chess move.
     *
     * @param move  the special move to validate
     * @param board the current board state
     * @return true if the special move is valid
     */
    public boolean validateSpecialMove(Move move, Board board) {
        switch (move.getMoveType()) {
            case CASTLING:
                return validateCastling(move, board);
            case EN_PASSANT:
                return validateEnPassant(move, board);
            case PROMOTION:
                return validatePromotion(move, board);
            default:
                return true; // Normal moves are already validated
        }
    }

    /**
     * Validates castling move.
     * Requirements:
     * - King and rook haven't moved
     * - No pieces between king and rook
     * - King is not in check
     * - King doesn't pass through or land on attacked square
     *
     * @param move  the castling move
     * @param board the current board state
     * @return true if castling is valid
     */
    private boolean validateCastling(Move move, Board board) {
        Piece king = move.getPiece();
        if (king.getType() != PieceType.KING || king.hasMoved()) {
            return false;
        }

        Position kingPos = move.getFrom();
        Position targetPos = move.getTo();
        int colDiff = targetPos.getCol() - kingPos.getCol();

        // Determine if kingside (right) or queenside (left) castling
        boolean isKingside = colDiff > 0;
        int rookCol = isKingside ? 7 : 0;
        Position rookPos = new Position(kingPos.getRow(), rookCol);

        Piece rook = board.getPiece(rookPos);
        if (rook == null || rook.getType() != PieceType.ROOK || rook.hasMoved()) {
            return false;
        }

        // Check if squares between king and rook are empty
        int start = Math.min(kingPos.getCol(), rookCol);
        int end = Math.max(kingPos.getCol(), rookCol);
        for (int col = start + 1; col < end; col++) {
            if (board.isOccupied(new Position(kingPos.getRow(), col))) {
                return false;
            }
        }

        // King cannot be in check, pass through check, or land in check
        CheckDetector checkDetector = new CheckDetector();
        Color opponentColor = king.getColor().opposite();

        // Check current position
        if (checkDetector.isInCheck(king.getColor(), board)) {
            return false;
        }

        // Check intermediate square
        int intermediateCol = isKingside ? kingPos.getCol() + 1 : kingPos.getCol() - 1;
        Position intermediatePos = new Position(kingPos.getRow(), intermediateCol);
        if (checkDetector.isPositionUnderAttack(intermediatePos, opponentColor, board)) {
            return false;
        }

        // Check target square
        if (checkDetector.isPositionUnderAttack(targetPos, opponentColor, board)) {
            return false;
        }

        return true;
    }

    /**
     * Validates en passant capture.
     * Requirements:
     * - Moving piece is a pawn
     * - Target square is empty
     * - Adjacent square contains opponent pawn that just moved two squares
     *
     * @param move  the en passant move
     * @param board the current board state
     * @return true if en passant is valid
     */
    private boolean validateEnPassant(Move move, Board board) {
        Piece piece = move.getPiece();
        if (piece.getType() != PieceType.PAWN) {
            return false;
        }

        Position from = move.getFrom();
        Position to = move.getTo();
        Position lastDoublePawn = board.getLastDoubleMovePawn();

        if (lastDoublePawn == null) {
            return false;
        }

        // Check if capturing pawn is adjacent to the double-moved pawn
        int capturedPawnCol = lastDoublePawn.getCol();
        int direction = (piece.getColor() == Color.WHITE) ? -1 : 1;
        Position expectedTarget = new Position(lastDoublePawn.getRow() + direction, capturedPawnCol);

        return to.equals(expectedTarget) && Math.abs(from.getCol() - capturedPawnCol) == 1;
    }

    /**
     * Validates pawn promotion.
     * Requirements:
     * - Moving piece is a pawn
     * - Pawn reaches opposite end of board
     * - Valid promotion piece type specified
     *
     * @param move  the promotion move
     * @param board the current board state
     * @return true if promotion is valid
     */
    private boolean validatePromotion(Move move, Board board) {
        Piece piece = move.getPiece();
        if (piece.getType() != PieceType.PAWN) {
            return false;
        }

        Pawn pawn = (Pawn) piece;
        Position to = move.getTo();

        // Check if pawn reaches promotion rank
        if (!pawn.canPromote(to)) {
            return false;
        }

        // Check if valid promotion type specified
        PieceType promoType = move.getPromotionType();
        return promoType == PieceType.QUEEN ||
                promoType == PieceType.ROOK ||
                promoType == PieceType.BISHOP ||
                promoType == PieceType.KNIGHT;
    }

    /**
     * Executes a castling move on the board.
     *
     * @param move  the castling move
     * @param board the board to modify
     */
    public void executeCastling(Move move, Board board) {
        Position kingPos = move.getFrom();
        Position targetPos = move.getTo();
        int colDiff = targetPos.getCol() - kingPos.getCol();

        boolean isKingside = colDiff > 0;
        int rookCol = isKingside ? 7 : 0;
        int newRookCol = isKingside ? targetPos.getCol() - 1 : targetPos.getCol() + 1;

        Position rookPos = new Position(kingPos.getRow(), rookCol);
        Position newRookPos = new Position(kingPos.getRow(), newRookCol);

        // Move king
        board.movePiece(kingPos, targetPos);

        // Move rook
        board.movePiece(rookPos, newRookPos);
    }

    /**
     * Executes an en passant capture on the board.
     *
     * @param move  the en passant move
     * @param board the board to modify
     */
    public void executeEnPassant(Move move, Board board) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Position capturedPawnPos = board.getLastDoubleMovePawn();

        // Move attacking pawn
        board.movePiece(from, to);

        // Remove captured pawn
        if (capturedPawnPos != null) {
            board.removePiece(capturedPawnPos);
        }
    }
}

