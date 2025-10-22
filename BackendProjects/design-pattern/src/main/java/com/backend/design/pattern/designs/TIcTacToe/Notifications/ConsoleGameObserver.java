package com.backend.design.pattern.designs.TIcTacToe.Notifications;

import com.backend.design.pattern.designs.TIcTacToe.model.Move;
import com.backend.design.pattern.designs.TIcTacToe.model.Player;

/**
 * Console-based observer implementation
 * Prints game events to console
 */
public class ConsoleGameObserver implements GameObserver {

    private static final String SEPARATOR = "==========================================";

    @Override
    public void onMoveMade(Move move, Player opponent) {
        System.out.println("\n[MOVE] " + move);
        if (opponent != null) {
            System.out.println("[NOTIFICATION] " + opponent.getPlayerName() + 
                ", it's your turn now!");
        }
    }

    @Override
    public void onPlayerWon(Player winner) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("🎉 CONGRATULATIONS! 🎉");
        System.out.println(winner.getPlayerName() + " (" + winner.getSymbol() + ") WON THE GAME!");
        System.out.println(SEPARATOR);
    }

    @Override
    public void onGameDraw() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("🤝 GAME DRAW! 🤝");
        System.out.println("The board is full with no winner.");
        System.out.println(SEPARATOR);
    }

    @Override
    public void onGameStart(Player player1, Player player2) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("🎮 GAME STARTED! 🎮");
        System.out.println("Player 1: " + player1);
        System.out.println("Player 2: " + player2);
        System.out.println(SEPARATOR);
    }

    @Override
    public void onGameReset() {
        System.out.println("\n[RESET] Game has been reset. Starting new game...");
    }
}

