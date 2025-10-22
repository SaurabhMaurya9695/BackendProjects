package com.backend.design.pattern.behavioural.state;

public class Client {

    public static void main(String[] args) {
        System.out.println("Start Playing with Vending Machine");
        int itemCount = 2;
        int itemPrice = 20;

        VendingMachine machine = new VendingMachine();
        machine.setItemCount(itemCount);
        machine.setItemPrice(itemPrice);

        // Test Scenario - Each Operation should be changing their state
        System.out.println("1 :Trying to select item without coin");
    }
}
