package com.backend.design.pattern.designs.TIcTacToe;

import com.backend.design.pattern.designs.TIcTacToe.model.Player;

import java.util.Scanner;

/**
 * Game Controller - handles user interaction
 * Single Responsibility: Manages user input/output
 * Separates UI concerns from game logic
 */
public class GameController {

    private final TicTacToeGame game;
    private final Scanner scanner;

    public GameController(TicTacToeGame game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        this.game = game;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the game loop
     */
    public void start() {
        boolean playAgain = true;

        while (playAgain) {
            playGame();
            playAgain = askPlayAgain();
            
            if (playAgain) {
                game.resetGame();
            }
        }

        System.out.println("\nThank you for playing TicTacToe!");
        printFinalScores();
        scanner.close();
    }

    /**
     * Plays a single game
     */
    private void playGame() {
        game.startGame();
        displayBoard();

        while (!game.isGameOver()) {
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println("\n" + currentPlayer.getPlayerName() + "'s turn (" + 
                currentPlayer.getSymbol() + ")");

            boolean validMove = false;
            while (!validMove) {
                try {
                    int[] move = getPlayerMove();
                    game.makeMove(move[0], move[1]);
                    displayBoard();
                    validMove = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ Invalid move: " + e.getMessage());
                    System.out.println("Please try again.");
                } catch (IllegalStateException e) {
                    System.out.println("❌ " + e.getMessage());
                    System.out.println("Please try again.");
                }
            }
        }
    }

    /**
     * Gets move input from the player
     */
    private int[] getPlayerMove() {
        System.out.print("Enter row (0-" + (game.getBoard().getRows() - 1) + "): ");
        int row = getValidInteger();

        System.out.print("Enter column (0-" + (game.getBoard().getCols() - 1) + "): ");
        int col = getValidInteger();

        return new int[]{row, col};
    }

    /**
     * Gets a valid integer input
     */
    private int getValidInteger() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    /**
     * Asks if players want to play again
     */
    private boolean askPlayAgain() {
        System.out.print("\nDo you want to play again? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    /**
     * Displays the current board
     */
    private void displayBoard() {
        System.out.println(game.getBoard());
    }

    /**
     * Prints final scores
     */
    private void printFinalScores() {
        System.out.println("\n========== FINAL SCORES ==========");
        System.out.println(game.getPlayer1().getPlayerName() + ": " + 
            game.getPlayer1().getScore() + " wins");
        System.out.println(game.getPlayer2().getPlayerName() + ": " + 
            game.getPlayer2().getScore() + " wins");
        System.out.println("==================================");
    }
}

