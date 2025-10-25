package com.backend.design.pattern.designs.ChessGame.ModularCode.game;

import com.backend.design.pattern.designs.ChessGame.ModularCode.model.GameResult;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;

/**
 * Observer interface for game events.
 * Implements the Observer Pattern to notify interested parties of game state changes.
 * Follows Interface Segregation Principle.
 */
public interface GameEventListener {

    /**
     * Called when a move is made in the game.
     *
     * @param move   the move that was made
     * @param matchId the ID of the match
     */
    void onMoveMade(Move move, String matchId);

    /**
     * Called when a player is in check.
     *
     * @param color   the color of the player in check
     * @param matchId the ID of the match
     */
    void onCheck(Color color, String matchId);

    /**
     * Called when the game ends.
     *
     * @param result  the result of the game
     * @param reason  the reason for game end (checkmate, stalemate, resignation, etc.)
     * @param matchId the ID of the match
     */
    void onGameEnd(GameResult result, String reason, String matchId);

    /**
     * Called when a player quits the game.
     *
     * @param playerName the name of the player who quit
     * @param matchId    the ID of the match
     */
    void onPlayerQuit(String playerName, String matchId);
}

