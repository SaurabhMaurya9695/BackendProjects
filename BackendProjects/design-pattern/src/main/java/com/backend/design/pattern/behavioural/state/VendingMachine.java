package com.backend.design.pattern.behavioural.state;

import com.backend.design.pattern.behavioural.state.VendingStates.*;

public class VendingMachine {

    private VendingState currentState;
    private int itemCount;
    private int itemPrice;
    private int insertedCoins;

    // State instances
    private final VendingState noCoinState;
    private final VendingState hasCoinState;
    private final VendingState dispenseState;
    private final VendingState soldOutState;

    public VendingMachine(int itemCount, int itemPrice) {
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
        this.insertedCoins = 0;

        // Initialize all states
        this.noCoinState = new NoCoinState();
        this.hasCoinState = new HasCoinState();
        this.dispenseState = new DispenseState();
        this.soldOutState = new SoldOutState();

        // Set initial state
        this.currentState = itemCount > 0 ? noCoinState : soldOutState;
    }

    // Delegate actions to current state
    public void insertCoin(int coin) {
        currentState = currentState.insertCoin(this, coin);
    }

    public void selectItem() {
        currentState = currentState.selectItem(this);
    }

    public void dispenseItem() {
        currentState = currentState.dispenseItem(this);
    }

    public void returnCoin() {
        currentState = currentState.returnCoin(this);
    }

    public void refill(int quantity) {
        currentState = currentState.refill(this, quantity);
    }

    // State management
    public VendingState getCurrentState() {
        return currentState;
    }

    public void setState(VendingState state) {
        this.currentState = state;
    }

    // Item management
    public int getItemCount() {
        return itemCount;
    }

    public void decrementItemCount() {
        if (itemCount > 0) {
            itemCount--;
        }
    }

    public void addItems(int quantity) {
        this.itemCount += quantity;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    // Coin management
    public int getInsertedCoins() {
        return insertedCoins;
    }

    public void addCoins(int coins) {
        this.insertedCoins += coins;
    }

    public void resetCoins() {
        this.insertedCoins = 0;
    }

    // State getters
    public VendingState getNoCoinState() {
        return noCoinState;
    }

    public VendingState getHasCoinState() {
        return hasCoinState;
    }

    public VendingState getDispenseState() {
        return dispenseState;
    }

    public VendingState getSoldOutState() {
        return soldOutState;
    }

    // Helper method to get current state name
    public String getCurrentStateName() {
        if (currentState == noCoinState) return "NoCoinState";
        if (currentState == hasCoinState) return "HasCoinState";
        if (currentState == dispenseState) return "DispenseState";
        if (currentState == soldOutState) return "SoldOutState";
        return "UnknownState";
    }
}
