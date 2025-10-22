package com.backend.design.pattern.designs.TIcTacToe.Notifications;

import com.backend.design.pattern.designs.TIcTacToe.model.Move;
import com.backend.design.pattern.designs.TIcTacToe.model.Player;

/**
 * Observer interface for receiving game notifications
 * Part of Observer Design Pattern
 */
public interface GameObserver {

    /**
     * Called when a player makes a move
     */
    void onMoveMade(Move move, Player opponent);

    /**
     * Called when a player wins
     */
    void onPlayerWon(Player winner);

    /**
     * Called when the game ends in a draw
     */
    void onGameDraw();

    /**
     * Called when the game starts
     */
    void onGameStart(Player player1, Player player2);

    /**
     * Called when the game is reset
     */
    void onGameReset();
}
