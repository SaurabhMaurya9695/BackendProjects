package com.backend.design.pattern.designs.TIcTacToe.model;

import java.util.Objects;

/**
 * Represents a symbol on the TicTacToe board (X, O, or empty)
 * Immutable class following value object pattern
 */
public class Symbol {

    private final Character symbol;
    public static final Symbol EMPTY = new Symbol('_');

    public Symbol() {
        this.symbol = '_';
    }

    public Symbol(Character symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol cannot be null");
        }
        this.symbol = symbol;
    }

    public Character getSymbol() {
        return symbol;
    }

    public boolean isEmpty() {
        return symbol == '_';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return Objects.equals(symbol, symbol1.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return symbol.toString();
    }
}
