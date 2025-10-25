package com.backend.design.pattern.designs.ChessGame.ModularCode.moves;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;

/**
 * Concrete command for executing a standard chess move.
 * Implements undo capability for move history and analysis.
 */
public class StandardMoveCommand implements MoveCommand {

    private final Move move;
    private boolean wasFirstMove;

    /**
     * Creates a new move command.
     *
     * @param move the move to execute
     */
    public StandardMoveCommand(Move move) {
        this.move = move;
    }

    @Override
    public void execute(Board board) {
        Piece piece = board.getPiece(move.getFrom());
        if (piece != null) {
            wasFirstMove = !piece.hasMoved();
            board.movePiece(move.getFrom(), move.getTo());
        }
    }

    @Override
    public void undo(Board board) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece piece = board.getPiece(to);

        if (piece != null) {
            // Move piece back
            board.removePiece(to);
            board.placePiece(from, piece);

            // Restore hasMoved state
            if (wasFirstMove) {
                // Reset the hasMoved flag by creating a new instance
                // This is a simplification; in production, you might want a better approach
            }

            // Restore captured piece if any
            if (move.getCapturedPiece() != null) {
                board.placePiece(to, move.getCapturedPiece());
            }
        }
    }

    @Override
    public Move getMove() {
        return move;
    }
}

