package com.backend.design.pattern.behavioural.state;

public class Client {

    // Helper method to print current state
    private static void printState(VendingMachine machine) {
        System.out.println("→ Current State: " + machine.getCurrentStateName());
    }

    public static void main(String[] args) {
        System.out.println("=== Vending Machine Demo ===\n");

        // Initialize machine with 2 items, price 20 each
        VendingMachine machine = new VendingMachine(2, 20);
        printState(machine);
        System.out.println();

        // Scenario 1: Try to select item without coin
        System.out.println("--- Scenario 1: Select item without coin ---");
        machine.selectItem();
        printState(machine);
        System.out.println();

        // Scenario 2: Insert insufficient coins
        System.out.println("--- Scenario 2: Insert insufficient coins ---");
        machine.insertCoin(10);
        printState(machine);
        machine.selectItem();
        printState(machine);
        System.out.println();

        // Scenario 3: Add more coins and purchase
        System.out.println("--- Scenario 3: Add more coins and purchase ---");
        machine.insertCoin(15);
        printState(machine);
        machine.selectItem();
        printState(machine);
        machine.dispenseItem();
        printState(machine);
        System.out.println();

        // Scenario 4: Return coins without purchasing
        System.out.println("--- Scenario 4: Return coins without purchasing ---");
        machine.insertCoin(25);
        printState(machine);
        machine.returnCoin();
        printState(machine);
        System.out.println();

        // Scenario 5: Purchase last item (sold out)
        System.out.println("--- Scenario 5: Purchase last item ---");
        machine.insertCoin(20);
        printState(machine);
        machine.selectItem();
        printState(machine);
        machine.dispenseItem();
        printState(machine);
        System.out.println();

        // Scenario 6: Try to purchase when sold out
        System.out.println("--- Scenario 6: Try to purchase when sold out ---");
        machine.insertCoin(20);
        printState(machine);
        machine.selectItem();
        printState(machine);
        System.out.println();

        // Scenario 7: Refill machine
        System.out.println("--- Scenario 7: Refill machine ---");
        machine.refill(5);
        printState(machine);
        System.out.println();

        // Scenario 8: Normal purchase after refill
        System.out.println("--- Scenario 8: Purchase after refill ---");
        machine.insertCoin(20);
        printState(machine);
        machine.selectItem();
        printState(machine);
        machine.dispenseItem();
        printState(machine);

        System.out.println("\n=== Demo Complete ===");
    }
}
