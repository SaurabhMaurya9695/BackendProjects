package com.backend.design.pattern.behavioural.state.VendingStates;

import com.backend.design.pattern.behavioural.state.VendingMachine;
import com.backend.design.pattern.behavioural.state.VendingState;

public class HasCoinState implements VendingState {

    @Override
    public VendingState insertCoin(VendingMachine vendingMachine, int coin) {
        vendingMachine.addCoin(coin);
        System.out.println("Adding Inserted Coin, Current Balance is : " + vendingMachine.getInsertedCoins());
        return vendingMachine.getHasCoinState(); // Transition into HasCoinState if you have
    }

    @Override
    public VendingState selectItem(VendingMachine vendingMachine) {
        // check the price, SelectedItemPrice and currentCoin which user gives should be more >=
        if (vendingMachine.getInsertedCoins() >= vendingMachine.getItemPrice()) {
            System.out.println("Starting Dispensing Item");
            int change = vendingMachine.getInsertedCoins() - vendingMachine.getItemPrice();
            if (change > 0) {
                System.out.println("returning : " + change + " money to user back");
            }
            vendingMachine.setInsertedCoins(0);
            return vendingMachine.getDispenseCoinState(); // changed the state
        } else {
            int needed = vendingMachine.getItemPrice() - vendingMachine.getInsertedCoins();
            System.out.println("Insufficient Balance : " + needed);
            return vendingMachine.getHasCoinState(); // stay in the same state
        }
    }

    @Override
    public VendingState dispenseItem(VendingMachine vendingMachine) {
        System.out.println("Please Insert Coin & Select Item first");
        return vendingMachine.getHasCoinState(); // State isn't changed
    }

    @Override
    public VendingState returnCoin(VendingMachine vendingMachine) {
        System.out.println("No Coins to return !!");
        return vendingMachine.getHasCoinState(); // State isn't changed
    }

    @Override
    public VendingState refill(VendingMachine vendingMachine, int quantity) {
        vendingMachine.getItemCount(quantity);
        return vendingMachine.getNoCoinState(); // State isn't changed
    }
}
