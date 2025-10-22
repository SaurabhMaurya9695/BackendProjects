package com.backend.design.pattern.behavioural.state.VendingStates;

import com.backend.design.pattern.behavioural.state.VendingMachine;
import com.backend.design.pattern.behavioural.state.VendingState;

public class NoCoinState implements VendingState {

    @Override
    public VendingState insertCoin(VendingMachine vendingMachine, int coin) {
        vendingMachine.setInsertedCoins(coin);
        return vendingMachine.getHasCoinState(); // Transition into HasCoinState if you have
    }

    @Override
    public VendingState selectItem(VendingMachine vendingMachine) {
        System.out.println("Please Insert Coin first");
        return vendingMachine.getNoCoinState(); // State isn't changed
    }

    @Override
    public VendingState dispenseItem(VendingMachine vendingMachine) {
        System.out.println("Please Insert Coin & Select Item first");
        return vendingMachine.getNoCoinState(); // State isn't changed
    }

    @Override
    public VendingState returnCoin(VendingMachine vendingMachine) {
        System.out.println("No Coins to return !!");
        return vendingMachine.getNoCoinState(); // State isn't changed
    }

    @Override
    public VendingState refill(VendingMachine vendingMachine, int quantity) {
        vendingMachine.getItemCount(quantity);
        return vendingMachine.getNoCoinState(); // State isn't changed
    }
}
