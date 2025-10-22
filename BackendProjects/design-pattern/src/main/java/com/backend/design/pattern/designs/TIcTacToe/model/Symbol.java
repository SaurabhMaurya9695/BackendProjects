package com.backend.design.pattern.designs.TIcTacToe.model;

public class Symbol {

    Character ch;

    public Symbol() {
        ch = '_';
    }

    public Symbol(Character ch) {
        this.ch = ch;
    }

    public Character getSymbol() {
        return ch;
    }

    public void setSymbol(Character ch) {
        this.ch = ch;
    }
}
