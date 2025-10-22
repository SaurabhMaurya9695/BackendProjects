package com.backend.design.pattern.designs.TIcTacToe.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a player in the TicTacToe game
 * Single Responsibility: Manages player identity and symbol
 */
public class Player {

    private final UUID playerID;
    private final String playerName;
    private final Symbol symbol;
    private int score;

    public Player(String playerName, Symbol symbol) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (symbol == null || symbol.isEmpty()) {
            throw new IllegalArgumentException("Player symbol cannot be null or empty");
        }
        
        this.playerID = UUID.randomUUID();
        this.playerName = playerName;
        this.symbol = symbol;
        this.score = 0;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(playerID, player.playerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID);
    }

    @Override
    public String toString() {
        return playerName + " (" + symbol + ")";
    }
}
