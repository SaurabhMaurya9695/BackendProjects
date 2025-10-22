package com.backend.design.pattern.behavioural.state.VendingStates;

import com.backend.design.pattern.behavioural.state.VendingMachine;
import com.backend.design.pattern.behavioural.state.VendingState;

public class DispenseState implements VendingState {

    @Override
    public VendingState insertCoin(VendingMachine machine, int coin) {
        System.out.println("Please wait, dispensing item...");
        return this;
    }

    @Override
    public VendingState selectItem(VendingMachine machine) {
        System.out.println("Already dispensing item...");
        return this;
    }

    @Override
    public VendingState dispenseItem(VendingMachine machine) {
        System.out.println("Dispensing item...");
        machine.decrementItemCount();
        System.out.println("Item dispensed! Enjoy your purchase.");
        System.out.println("Remaining items: " + machine.getItemCount());

        // Check if sold out
        if (machine.getItemCount() <= 0) {
            System.out.println("Machine is now sold out!");
            return machine.getSoldOutState();
        }
        return machine.getNoCoinState();
    }

    @Override
    public VendingState returnCoin(VendingMachine machine) {
        System.out.println("Cannot return coins during dispensing!");
        return this;
    }

    @Override
    public VendingState refill(VendingMachine machine, int quantity) {
        System.out.println("Cannot refill while dispensing!");
        return this;
    }
}

