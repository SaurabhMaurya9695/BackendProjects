package com.backend.design.pattern.designs.TIcTacToe.Strategy;

import com.backend.design.pattern.designs.TIcTacToe.model.Board;
import com.backend.design.pattern.designs.TIcTacToe.model.Symbol;

public interface Rules {

    boolean checkWins(Board board, Symbol symbol);

    boolean checkDraw(Board board, Symbol symbol);

    boolean isValidMove(Board board, int r , int c);
}
