package com.backend.design.pattern.designs.TIcTacToe.Strategy;

import com.backend.design.pattern.designs.TIcTacToe.model.Board;
import com.backend.design.pattern.designs.TIcTacToe.model.Symbol;

/**
 * Standard win strategy for TicTacToe
 * Checks for wins in rows, columns, and diagonals
 */
public class StandardWinStrategy implements WinStrategy {

    @Override
    public boolean checkWin(Board board, Symbol symbol) {
        return checkRows(board, symbol) || 
               checkColumns(board, symbol) || 
               checkDiagonals(board, symbol);
    }

    private boolean checkRows(Board board, Symbol symbol) {
        int size = board.getRows();
        for (int row = 0; row < size; row++) {
            boolean rowWin = true;
            for (int col = 0; col < size; col++) {
                if (!board.getCell(row, col).equals(symbol)) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns(Board board, Symbol symbol) {
        int size = board.getCols();
        for (int col = 0; col < size; col++) {
            boolean colWin = true;
            for (int row = 0; row < size; row++) {
                if (!board.getCell(row, col).equals(symbol)) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals(Board board, Symbol symbol) {
        int size = board.getRows();
        
        // Check main diagonal (top-left to bottom-right)
        boolean mainDiagonal = true;
        for (int i = 0; i < size; i++) {
            if (!board.getCell(i, i).equals(symbol)) {
                mainDiagonal = false;
                break;
            }
        }
        if (mainDiagonal) {
            return true;
        }

        // Check anti-diagonal (top-right to bottom-left)
        boolean antiDiagonal = true;
        for (int i = 0; i < size; i++) {
            if (!board.getCell(i, size - i - 1).equals(symbol)) {
                antiDiagonal = false;
                break;
            }
        }
        
        return antiDiagonal;
    }
}

