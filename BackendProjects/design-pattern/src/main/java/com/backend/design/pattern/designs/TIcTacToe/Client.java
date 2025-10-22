package com.backend.design.pattern.designs.TIcTacToe;

import com.backend.design.pattern.designs.TIcTacToe.model.Players;
import com.backend.design.pattern.designs.TIcTacToe.model.Symbol;

public class Client {

    public static void main(String[] args) {
        try {
            // create a player
            Players saurabh = new Players(new Symbol('*'));
            Players yash = new Players(new Symbol('x'));

            TicTacToe ticTacToe = new TicTacToe();
            ticTacToe.addPlayers(saurabh);
            ticTacToe.addPlayers(yash);

            ticTacToe.printBoard(); // it should be printing with a blank value
            ticTacToe.createBoard(3, 3);
            ticTacToe.printBoard();


        } catch (Exception e) {
            System.out.println("Some Exception Occurred");
        }
    }
}
