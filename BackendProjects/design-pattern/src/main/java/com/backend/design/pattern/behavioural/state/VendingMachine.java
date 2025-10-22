package com.backend.design.pattern.behavioural.state;

public class VendingMachine {

    private VendingState _vendingState;
    private int _itemCount;
    private int _itemPrice;
    private int _insertedCoins;

    private VendingState noCoinState;
    private VendingState hasCoinState;
    private VendingState dispenseCoinState;
    private VendingState soldOutCoinState;

    public VendingMachine() {
    }

    public VendingState getVendingState() {
        return _vendingState;
    }

    public void setVendingState(VendingState vendingState) {
        _vendingState = vendingState;
    }

    public int getItemCount(int q) {
        return _itemCount += q;
    }

    public void setItemCount(int itemCount) {
        _itemCount = itemCount;
    }

    public int getItemPrice() {
        return _itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        _itemPrice = itemPrice;
    }

    public int getInsertedCoins() {
        return _insertedCoins;
    }

    public void setInsertedCoins(int insertedCoins) {
        _insertedCoins = insertedCoins;
    }

    public VendingState getNoCoinState() {
        return noCoinState;
    }

    public void setNoCoinState(VendingState noCoinState) {
        this.noCoinState = noCoinState;
    }

    public VendingState getHasCoinState() {
        return hasCoinState;
    }

    public void setHasCoinState(VendingState hasCoinState) {
        this.hasCoinState = hasCoinState;
    }

    public VendingState getDispenseCoinState() {
        return dispenseCoinState;
    }

    public void setDispenseCoinState(VendingState dispenseCoinState) {
        this.dispenseCoinState = dispenseCoinState;
    }

    public VendingState getSoldOutCoinState() {
        return soldOutCoinState;
    }

    public void setSoldOutCoinState(VendingState soldOutCoinState) {
        this.soldOutCoinState = soldOutCoinState;
    }

    public void addCoin(int coin) {
        this._insertedCoins += coin;
    }
}
