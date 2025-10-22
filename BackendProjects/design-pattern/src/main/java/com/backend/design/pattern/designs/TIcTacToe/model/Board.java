package com.backend.design.pattern.designs.TIcTacToe.model;

/**
 * Represents the TicTacToe board
 * Single Responsibility: Manages board state and cell operations
 */
public class Board {

    private final int rows;
    private final int cols;
    private final Symbol[][] cells;
    private int filledCells;

    public Board(int size) {
        this(size, size);
    }

    public Board(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Board dimensions must be positive");
        }
        if (rows < 3 || cols < 3) {
            throw new IllegalArgumentException("Board must be at least 3x3");
        }
        
        this.rows = rows;
        this.cols = cols;
        this.cells = new Symbol[rows][cols];
        this.filledCells = 0;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = Symbol.EMPTY;
            }
        }
    }

    /**
     * Makes a move on the board
     * @throws IllegalArgumentException if position is invalid
     * @throws IllegalStateException if cell is already occupied
     */
    public void makeMove(int row, int col, Symbol symbol) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException(
                String.format("Invalid position: [%d, %d]. Must be within [0-%d, 0-%d]", 
                    row, col, rows-1, cols-1)
            );
        }
        if (!isCellEmpty(row, col)) {
            throw new IllegalStateException(
                String.format("Cell [%d, %d] is already occupied", row, col)
            );
        }
        
        cells[row][col] = symbol;
        filledCells++;
    }

    public Symbol getCell(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException(
                String.format("Invalid position: [%d, %d]", row, col)
            );
        }
        return cells[row][col];
    }

    public boolean isCellEmpty(int row, int col) {
        return getCell(row, col).isEmpty();
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isFull() {
        return filledCells == (rows * cols);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void reset() {
        initializeBoard();
        filledCells = 0;
    }

    public String getBoardState() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        
        // Print column numbers
        sb.append("   ");
        for (int j = 0; j < cols; j++) {
            sb.append(" ").append(j).append(" ");
        }
        sb.append("\n");
        
        for (int i = 0; i < rows; i++) {
            sb.append(i).append(" | ");
            for (int j = 0; j < cols; j++) {
                sb.append(cells[i][j]).append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getBoardState();
    }
}
