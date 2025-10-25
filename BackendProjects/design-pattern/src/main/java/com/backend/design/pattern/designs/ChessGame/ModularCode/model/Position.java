package com.backend.design.pattern.designs.ChessGame.ModularCode.model;

import java.util.Objects;

/**
 * Immutable value object representing a position on the chess board.
 * Uses 0-indexed coordinates where (0,0) is a8 and (7,7) is h1.
 * This class follows the Value Object pattern and is immutable.
 */
public final class Position {

    private final int row;
    private final int col;

    /**
     * Creates a new position with the specified row and column.
     *
     * @param row the row (0-7, where 0 is rank 8)
     * @param col the column (0-7, where 0 is file 'a')
     * @throws IllegalArgumentException if row or col is out of bounds
     */
    public Position(int row, int col) {
        if (!isValidCoordinate(row, col)) {
            throw new IllegalArgumentException(
                    "Invalid position: row=" + row + ", col=" + col + ". Must be between 0-7.");
        }
        this.row = row;
        this.col = col;
    }

    /**
     * Checks if the given coordinates are valid chess board positions.
     *
     * @param row the row to check
     * @param col the column to check
     * @return true if both coordinates are between 0 and 7 (inclusive)
     */
    private static boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * Creates a Position from chess notation (e.g., "e4", "a8").
     *
     * @param notation the chess notation (e.g., "e4")
     * @return the corresponding Position
     * @throws IllegalArgumentException if notation is invalid
     */
    public static Position fromNotation(String notation) {
        if (notation == null || notation.length() != 2) {
            throw new IllegalArgumentException("Invalid chess notation: " + notation);
        }

        char file = Character.toLowerCase(notation.charAt(0));
        char rank = notation.charAt(1);

        if (file < 'a' || file > 'h' || rank < '1' || rank > '8') {
            throw new IllegalArgumentException("Invalid chess notation: " + notation);
        }

        int col = file - 'a';
        int row = '8' - rank;

        return new Position(row, col);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Converts this position to standard chess notation (e.g., "e4", "a8").
     *
     * @return the chess notation string
     */
    public String toChessNotation() {
        char file = (char) ('a' + col);
        char rank = (char) ('8' - row);
        return "" + file + rank;
    }

    /**
     * Creates a new Position offset by the given deltas.
     *
     * @param deltaRow the row offset
     * @param deltaCol the column offset
     * @return a new Position, or null if the result is out of bounds
     */
    public Position offset(int deltaRow, int deltaCol) {
        int newRow = row + deltaRow;
        int newCol = col + deltaCol;
        if (isValidCoordinate(newRow, newCol)) {
            return new Position(newRow, newCol);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return toChessNotation();
    }
}

