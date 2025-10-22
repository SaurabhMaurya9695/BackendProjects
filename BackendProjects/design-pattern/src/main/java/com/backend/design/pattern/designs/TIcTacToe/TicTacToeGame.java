package com.backend.design.pattern.designs.TIcTacToe;

import com.backend.design.pattern.designs.TIcTacToe.Notifications.GameObserver;
import com.backend.design.pattern.designs.TIcTacToe.Notifications.GameSubject;
import com.backend.design.pattern.designs.TIcTacToe.Strategy.WinStrategy;
import com.backend.design.pattern.designs.TIcTacToe.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Main TicTacToe Game Engine
 * <p>
 * Follows SOLID Principles:
 * - Single Responsibility: Manages game flow and state
 * - Open/Closed: Open for extension via WinStrategy
 * - Dependency Inversion: Depends on abstractions (WinStrategy, GameObserver)
 * <p>
 * Design Patterns Used:
 * - Strategy Pattern: For win condition checking
 * - Observer Pattern: For game notifications
 */
public class TicTacToeGame implements GameSubject {

    private final Board board;
    private final WinStrategy winStrategy;
    private final List<GameObserver> observers;
    private final Player player1;
    private final Player player2;

    private GameState gameState;
    private Player currentPlayer;
    private Player winner;

    /**
     * Constructor with Dependency Injection
     * Follows Dependency Inversion Principle
     */
    public TicTacToeGame(Board board, Player player1, Player player2, WinStrategy winStrategy) {
        if (board == null || player1 == null || player2 == null || winStrategy == null) {
            throw new IllegalArgumentException("All game components must be non-null");
        }
        if (player1.getSymbol().equals(player2.getSymbol())) {
            throw new IllegalArgumentException("Players must have different symbols");
        }

        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.winStrategy = winStrategy;
        this.observers = new ArrayList<>();
        this.gameState = GameState.NOT_STARTED;
        this.currentPlayer = player1;
    }

    /**
     * Starts the game
     */
    public void startGame() {
        if (gameState == GameState.IN_PROGRESS) {
            throw new IllegalStateException("Game is already in progress");
        }

        gameState = GameState.IN_PROGRESS;
        currentPlayer = player1;
        winner = null;

        notifyGameStart();
    }

    /**
     * Makes a move for the current player
     *
     * @throws IllegalStateException    if game is not in progress
     * @throws IllegalArgumentException if move is invalid
     */
    public void makeMove(int row, int col) {
        if (gameState != GameState.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        // Make the move on the board (board validates the move)
        board.makeMove(row, col, currentPlayer.getSymbol());

        // Create move object
        Move move = new Move(row, col, currentPlayer);

        // Check for win
        if (winStrategy.checkWin(board, currentPlayer.getSymbol())) {
            gameState = GameState.PLAYER_WON;
            winner = currentPlayer;
            currentPlayer.incrementScore();
            notifyPlayerWon(winner);
            return;
        }

        // Check for draw
        if (board.isFull()) {
            gameState = GameState.DRAW;
            notifyGameDraw();
            return;
        }

        // Switch player and notify
        Player nextPlayer = getOpponent(currentPlayer);
        notifyMoveMade(move, nextPlayer);
        currentPlayer = nextPlayer;
    }

    /**
     * Resets the game for a new round
     */
    public void resetGame() {
        board.reset();
        gameState = GameState.NOT_STARTED;
        currentPlayer = player1;
        winner = null;
        notifyGameReset();
    }

    // Getters
    public GameState getGameState() {
        return gameState;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isGameOver() {
        return gameState == GameState.PLAYER_WON || gameState == GameState.DRAW;
    }

    private Player getOpponent(Player player) {
        return player.equals(player1) ? player2 : player1;
    }

    // Observer Pattern Implementation
    @Override
    public void attach(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(GameObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        // Generic notify method - not used in this implementation
        // Specific notification methods are used instead
    }

    private void notifyMoveMade(Move move, Player opponent) {
        for (GameObserver observer : observers) {
            observer.onMoveMade(move, opponent);
        }
    }

    private void notifyPlayerWon(Player winner) {
        for (GameObserver observer : observers) {
            observer.onPlayerWon(winner);
        }
    }

    private void notifyGameDraw() {
        for (GameObserver observer : observers) {
            observer.onGameDraw();
        }
    }

    private void notifyGameStart() {
        for (GameObserver observer : observers) {
            observer.onGameStart(player1, player2);
        }
    }

    private void notifyGameReset() {
        for (GameObserver observer : observers) {
            observer.onGameReset();
        }
    }
}
