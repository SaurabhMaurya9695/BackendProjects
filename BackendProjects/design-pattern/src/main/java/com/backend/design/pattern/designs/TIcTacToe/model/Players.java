package com.backend.design.pattern.designs.TIcTacToe.model;

import java.util.UUID;

public class Players {

    private final UUID playerID;
    private int score;

    private Board _board;
    private Symbol _symbol;

    public Players(Symbol symbol) {
        playerID = UUID.randomUUID();
        this._symbol = symbol;
    }

    public Symbol getSymbol() {
        return _symbol;
    }

    public void setSymbol(Symbol symbol) {
        _symbol = symbol;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

//    public void makeMove(Board board , int r , int c)
}
