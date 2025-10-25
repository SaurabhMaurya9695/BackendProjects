package com.backend.design.pattern.designs.ChessGame.ModularCode.moves;

import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tracks the history of moves in a game.
 * Provides access to past moves for analysis, undo, and game replay.
 */
public class MoveHistory {

    private final List<Move> moves;

    /**
     * Creates a new empty move history.
     */
    public MoveHistory() {
        this.moves = new ArrayList<>();
    }

    /**
     * Adds a move to the history.
     *
     * @param move the move to add
     */
    public void addMove(Move move) {
        moves.add(move);
    }

    /**
     * Gets the last move made, or null if no moves have been made.
     *
     * @return the last move, or null
     */
    public Move getLastMove() {
        if (moves.isEmpty()) {
            return null;
        }
        return moves.get(moves.size() - 1);
    }

    /**
     * Gets a move at a specific index.
     *
     * @param index the index of the move
     * @return the move at that index
     */
    public Move getMoveAt(int index) {
        return moves.get(index);
    }

    /**
     * Gets the total number of moves.
     *
     * @return the move count
     */
    public int getMoveCount() {
        return moves.size();
    }

    /**
     * Gets all moves in the history.
     *
     * @return unmodifiable list of all moves
     */
    public List<Move> getAllMoves() {
        return Collections.unmodifiableList(moves);
    }

    /**
     * Clears all moves from history.
     */
    public void clear() {
        moves.clear();
    }

    /**
     * Returns a string representation of the move history in algebraic notation.
     *
     * @return formatted move history
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                sb.append((i / 2 + 1)).append(". ");
            }
            sb.append(moves.get(i).toString()).append(" ");
        }
        return sb.toString().trim();
    }
}

