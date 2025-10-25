package com.backend.design.pattern.designs.ChessGame.ModularCode.moves;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;

/**
 * Command interface for executing chess moves.
 * Implements the Command Pattern for move execution and potential undo/redo.
 */
public interface MoveCommand {

    /**
     * Executes the move on the board.
     *
     * @param board the board to modify
     */
    void execute(Board board);

    /**
     * Undoes the move on the board.
     *
     * @param board the board to restore
     */
    void undo(Board board);

    /**
     * Gets the move associated with this command.
     *
     * @return the move
     */
    Move getMove();
}

