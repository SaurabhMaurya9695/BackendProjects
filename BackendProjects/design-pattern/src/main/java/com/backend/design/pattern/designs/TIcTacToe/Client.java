package com.backend.design.pattern.designs.TIcTacToe;

import com.backend.design.pattern.designs.TIcTacToe.Notifications.ConsoleGameObserver;
import com.backend.design.pattern.designs.TIcTacToe.Notifications.GameObserver;
import com.backend.design.pattern.designs.TIcTacToe.Strategy.StandardWinStrategy;
import com.backend.design.pattern.designs.TIcTacToe.Strategy.WinStrategy;
import com.backend.design.pattern.designs.TIcTacToe.model.Board;
import com.backend.design.pattern.designs.TIcTacToe.model.Player;
import com.backend.design.pattern.designs.TIcTacToe.model.Symbol;

/**
 * Client class demonstrating TicTacToe game with SOLID principles
 * <p>
 * Key Features:
 * 1. Dependency Injection - all dependencies are injected
 * 2. Observer Pattern - opponent gets notified on every move
 * 3. Strategy Pattern - win condition checking is pluggable
 * 4. Follows all SOLID principles
 * 5. Clean separation of concerns
 */
public class Client {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   Welcome to TicTacToe Game!           ║");
        System.out.println("║   Built with SOLID Principles          ║");
        System.out.println("╚════════════════════════════════════════╝");

        try {
            // Create game components with Dependency Injection
            Board board = createBoard();
            Player player1 = createPlayer1();
            Player player2 = createPlayer2();
            WinStrategy winStrategy = createWinStrategy();

            // Create game with all dependencies injected
            TicTacToeGame game = new TicTacToeGame(board, player1, player2, winStrategy);

            // Attach observers (opponent notification)
            GameObserver consoleObserver = new ConsoleGameObserver();
            game.attach(consoleObserver);

            // Create controller and start the game
            GameController controller = new GameController(game);
            controller.start();
        } catch (Exception e) {
            System.err.println("❌ An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Factory method to create board
     * Can be easily modified to create different board sizes
     */
    private static Board createBoard() {
        return new Board(3, 3); // Standard 3x3 TicTacToe board
    }

    /**
     * Factory method to create player 1
     */
    private static Player createPlayer1() {
        return new Player("Player 1", new Symbol('X'));
    }

    /**
     * Factory method to create player 2
     */
    private static Player createPlayer2() {
        return new Player("Player 2", new Symbol('O'));
    }

    /**
     * Factory method to create win strategy
     * Can be easily swapped with different strategies
     */
    private static WinStrategy createWinStrategy() {
        return new StandardWinStrategy();
    }
}

