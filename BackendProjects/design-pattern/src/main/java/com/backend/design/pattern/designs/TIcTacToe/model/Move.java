package com.backend.design.pattern.designs.TIcTacToe.model;

import java.util.Objects;

/**
 * Value object representing a move in TicTacToe
 * Immutable class following value object pattern
 */
public class Move {

    private final int row;
    private final int col;
    private final Player player;

    public Move(int row, int col, Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.row = row;
        this.col = col;
        this.player = player;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Player getPlayer() {
        return player;
    }

    public Symbol getSymbol() {
        return player.getSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row && col == move.col && Objects.equals(player, move.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, player);
    }

    @Override
    public String toString() {
        return String.format("%s placed %s at [%d, %d]", 
            player.getPlayerName(), player.getSymbol(), row, col);
    }
}

