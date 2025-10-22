package com.backend.design.pattern.designs.TIcTacToe;

import com.backend.design.pattern.designs.TIcTacToe.model.Board;
import com.backend.design.pattern.designs.TIcTacToe.model.Symbol;

public class TicTacToeUtil {

    // Checks if the given symbol has won diagonally (either main or anti-diagonal)
    public static boolean checkWinInDiagonal(Board board, Symbol s) {
        int n = board.getRowSize();

        boolean mainDiagonal = true;
        boolean antiDiagonal = true;

        for (int i = 0; i < n; i++) {
            // Check main diagonal (i, i)
            if (!board.getCell(i, i).equals(s)) {
                mainDiagonal = false;
            }

            // Check anti-diagonal (i, n - i - 1)
            if (!board.getCell(i, i).equals(s)) {
                antiDiagonal = false;
            }
        }

        return mainDiagonal || antiDiagonal;
    }

    public static boolean checkWinInRowAndCol(Board board, Symbol symbol) {
        int n = board.getRowSize();
        String sym = symbol.getSymbol().toString();

        // Check all rows
        for (int i = 0; i < n; i++) {
            boolean rowWin = true;
            for (int j = 0; j < n; j++) {
                if (!board.getCell(i, j).equals(sym)) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                return true;
            }
        }

        // Check all columns
        for (int j = 0; j < n; j++) {
            boolean colWin = true;
            for (int i = 0; i < n; i++) {
                if (!board.getCell(i, j).equals(sym)) {
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
}
