package com.backend.design.pattern.designs.TIcTacToe.Strategy;

import com.backend.design.pattern.designs.TIcTacToe.model.Board;
import com.backend.design.pattern.designs.TIcTacToe.model.Symbol;

/**
 * Strategy interface for win condition validation
 * Follows Strategy Design Pattern - allows different win rules
 * Follows Interface Segregation Principle - focused interface
 */
public interface WinStrategy {

    /**
     * Checks if the given symbol has won the game
     * @param board The game board
     * @param symbol The symbol to check for winning
     * @return true if the symbol has won, false otherwise
     */
    boolean checkWin(Board board, Symbol symbol);
}
