package com.backend.design.pattern.designs.ChessGame.ModularCode.rules;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;

/**
 * Interface defining chess rules and game state validation.
 * Follows the Strategy Pattern - different rule implementations can be swapped.
 * Adheres to Interface Segregation Principle.
 */
public interface ChessRules {

    /**
     * Validates if a move is legal according to chess rules.
     *
     * @param move  the move to validate
     * @param board the current board state
     * @return true if the move is valid
     */
    boolean isValidMove(Move move, Board board);

    /**
     * Checks if the specified color's king is in check.
     *
     * @param color the color to check
     * @param board the current board state
     * @return true if the king is in check
     */
    boolean isInCheck(Color color, Board board);

    /**
     * Checks if the specified color is in checkmate.
     *
     * @param color the color to check
     * @param board the current board state
     * @return true if the color is in checkmate
     */
    boolean isCheckmate(Color color, Board board);

    /**
     * Checks if the specified color is in stalemate.
     *
     * @param color the color to check
     * @param board the current board state
     * @return true if the color is in stalemate
     */
    boolean isStalemate(Color color, Board board);

    /**
     * Checks if executing a move would put the moving player's king in check.
     *
     * @param move      the move to test
     * @param board     the current board state
     * @param kingColor the color of the king to protect
     * @return true if the move would result in check
     */
    boolean wouldMoveCauseCheck(Move move, Board board, Color kingColor);
}

