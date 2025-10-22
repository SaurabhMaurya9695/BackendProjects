package com.backend.design.pattern.designs.TIcTacToe.Notifications;

public class ConsoleObsever implements IObserver {

    @Override
    public void update(String msg) {
        System.out.println("[ConsoleObsever] : " + msg);
    }
}
