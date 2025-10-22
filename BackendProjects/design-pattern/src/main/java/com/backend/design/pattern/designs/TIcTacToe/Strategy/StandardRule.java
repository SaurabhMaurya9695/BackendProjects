package com.backend.design.pattern.designs.TIcTacToe.Strategy;

import com.backend.design.pattern.designs.TIcTacToe.TicTacToeUtil;
import com.backend.design.pattern.designs.TIcTacToe.model.Board;
import com.backend.design.pattern.designs.TIcTacToe.model.Symbol;

public class StandardRule implements Rules {

    @Override
    public boolean checkWins(Board board, Symbol symbol) {
        // if in any position row wise, col wise and diagonal wise if we found this char
        // it means we won
        boolean winInDiagonal = TicTacToeUtil.checkWinInDiagonal(board, symbol);
        if (winInDiagonal) {
            return true;
        }

        // no winner in diagonal, check in row and col
        return TicTacToeUtil.checkWinInRowAndCol(board, symbol);
    }

    @Override
    public boolean checkDraw(Board board, Symbol symbol) {
        return !checkWins(board, symbol);
    }

    @Override
    public boolean isValidMove(Board board, int r, int c) {
        Symbol cell = board.getCell(r, c);
        return !(cell.getSymbol().toString().equals("_"));
    }
}
