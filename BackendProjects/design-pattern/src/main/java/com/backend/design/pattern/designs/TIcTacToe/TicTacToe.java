package com.backend.design.pattern.designs.TIcTacToe;

import com.backend.design.pattern.designs.TIcTacToe.Notifications.ConsoleObsever;
import com.backend.design.pattern.designs.TIcTacToe.Notifications.IObserver;
import com.backend.design.pattern.designs.TIcTacToe.Strategy.Rules;
import com.backend.design.pattern.designs.TIcTacToe.Strategy.StandardRule;
import com.backend.design.pattern.designs.TIcTacToe.model.Board;
import com.backend.design.pattern.designs.TIcTacToe.model.Players;
import com.backend.design.pattern.designs.TIcTacToe.model.Symbol;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TicTacToe {

    // we have to play 0 & X, so we need it board first
    Board _board;

    Rules _rules;

    Deque<Players> _players;

    boolean _isGameOver;

    // this class will become an Observable and will send notification since this class have the
    // method play and each turn

    List<IObserver> _observables;

    TicTacToe() {
        _observables = new ArrayList<>();
        _observables.add(new ConsoleObsever());
        _isGameOver = false;
        _players = new ArrayDeque<>();
        _rules = new StandardRule();
        _board = new Board();
    }

    public void createBoard(int r, int c) {
        _board = new Board(r, c);
    }

    private void notifyObservers(String msg) {
        for (IObserver observer : _observables) {
            observer.update(msg);
        }
    }

    private void addObservers(IObserver observer) {
        _observables.add(observer);
    }

    private void removeObservers(IObserver observer) {
        _observables.remove(observer);
    }

    public void addPlayers(Players players) {
        _players.add(players);
    }

    public void play(int r, int c) {
        while (!_isGameOver) {

            // play until the game is over
            Players playersFirst = _players.getFirst();
            _players.removeFirst();
            // now we have player
            // make the move now at r, c. before making move we should check 
            Symbol cell = _board.getCell(r, c);
            // before making the move again
            boolean wins = _rules.checkWins(_board, cell);
            if (wins) {
                _isGameOver = true;
            }

            // check for draw
            boolean draw = _rules.checkDraw(_board, cell);
            if (draw) {
                _isGameOver = true;
            }

            // now the cell should be empty
            if (_board.isCellEmpty(cell)) {
                // now make the move now
                _board.makeMove(cell, r, c);
                String p1 =
                        "Players with ID " + playersFirst.getPlayerID() + " has done move at {" + r + ", " + c + "}";
                notifyObservers(p1);
            }
            _players.addLast(playersFirst);
        }
    }

    public void printBoard() {
        _board.printBoard();
    }
}
