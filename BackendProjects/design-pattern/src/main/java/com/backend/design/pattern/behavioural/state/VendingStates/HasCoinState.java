package com.backend.design.pattern.behavioural.state.VendingStates;

import com.backend.design.pattern.behavioural.state.VendingMachine;
import com.backend.design.pattern.behavioural.state.VendingState;

public class HasCoinState implements VendingState {

    @Override
    public VendingState insertCoin(VendingMachine machine, int coin) {
        System.out.println("Adding more coins: " + coin);
        machine.addCoins(coin);
        System.out.println("Current balance: " + machine.getInsertedCoins());
        return this;
    }

    @Override
    public VendingState selectItem(VendingMachine machine) {
        int balance = machine.getInsertedCoins();
        int price = machine.getItemPrice();

        if (balance >= price) {
            System.out.println("Item selected. Price: " + price);
            int change = balance - price;
            if (change > 0) {
                System.out.println("Returning change: " + change);
            }
            machine.resetCoins();
            return machine.getDispenseState();
        } else {
            int needed = price - balance;
            System.out.println("Insufficient balance! Need " + needed + " more");
            return this;
        }
    }

    @Override
    public VendingState dispenseItem(VendingMachine machine) {
        System.out.println("Please select item first!");
        return this;
    }

    @Override
    public VendingState returnCoin(VendingMachine machine) {
        int coins = machine.getInsertedCoins();
        System.out.println("Returning coins: " + coins);
        machine.resetCoins();
        return machine.getNoCoinState();
    }

    @Override
    public VendingState refill(VendingMachine machine, int quantity) {
        System.out.println("Cannot refill while transaction is in progress");
        return this;
    }
}
