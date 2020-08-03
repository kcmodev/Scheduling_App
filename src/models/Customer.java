package models;

import javafx.collections.ObservableArray;

public class Customer{
    private String name;
    private int customerID;
    private int addressID;
    private int isActive;

    public Customer(String name, int customerID, int addressID, int isActive) {
        this.name = name;
        this.customerID = customerID;
        this.addressID = addressID;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
