package com.backend.design.pattern.behavioural.state;

public interface VendingState {

    VendingState insertCoin(VendingMachine vendingMachine, int coin);

    VendingState selectItem(VendingMachine vendingMachine);

    VendingState dispenseItem(VendingMachine vendingMachine);

    VendingState returnCoin(VendingMachine vendingMachine);

    VendingState refill(VendingMachine vendingMachine, int quantity);
}
