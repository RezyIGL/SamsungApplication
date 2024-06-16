package com.rezyjs.samsungapplication;

public class Item {

    String typeOfOperation;
    String moneyToShow;

    public Item(String typeOfOperation, String moneyToShow) {
        this.typeOfOperation = typeOfOperation;
        this.moneyToShow = moneyToShow;
    }

    public String getTypeOfOperation() {
        return typeOfOperation;
    }

    public void setTypeOfOperation(String typeOfOperation) {
        this.typeOfOperation = typeOfOperation;
    }

    public String getMoneyToShow() {
        return moneyToShow;
    }

    public void setMoneyToShow(String moneyToShow) {
        this.moneyToShow = moneyToShow;
    }
}
