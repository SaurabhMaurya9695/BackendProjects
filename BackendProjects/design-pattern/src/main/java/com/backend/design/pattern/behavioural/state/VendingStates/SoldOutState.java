package com.backend.design.pattern.behavioural.state.VendingStates;

import com.backend.design.pattern.behavioural.state.VendingMachine;
import com.backend.design.pattern.behavioural.state.VendingState;

public class SoldOutState implements VendingState {

    @Override
    public VendingState insertCoin(VendingMachine machine, int coin) {
        System.out.println("Machine is sold out! Cannot accept coins.");
        System.out.println("Returning coin: " + coin);
        return this;
    }

    @Override
    public VendingState selectItem(VendingMachine machine) {
        System.out.println("Machine is sold out! No items available.");
        return this;
    }

    @Override
    public VendingState dispenseItem(VendingMachine machine) {
        System.out.println("Machine is sold out! Cannot dispense.");
        return this;
    }

    @Override
    public VendingState returnCoin(VendingMachine machine) {
        System.out.println("No coins to return!");
        return this;
    }

    @Override
    public VendingState refill(VendingMachine machine, int quantity) {
        System.out.println("Refilling " + quantity + " items");
        machine.addItems(quantity);
        System.out.println("Machine restocked! Total items: " + machine.getItemCount());
        return machine.getNoCoinState();
    }
}

