package com.backend.design.pattern.structural.facadePattern;

public class ClientCode {

    public static void main(String[] args) {
        ComputerFacade computerFacade = new ComputerFacade();
        computerFacade.startComputer();
    }
}
