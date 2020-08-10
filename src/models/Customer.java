package models;

import dao.CustomerDAO;

import java.sql.*;

public class Customer{
    private static CustomerDAO customerData = new CustomerDAO();

    private String name;
    private int customerId;
    private int addressId;
    private int isActive;

    public Customer(String name, int customerId, int addressID, int isActive) {
        this.name = name;
        this.customerId = customerId;
        this.addressId = addressID;
        this.isActive = isActive;
    }

    public Customer(String name, int customerId, int addressId) {
        this.name = name;
        this.customerId = customerId;
        this.addressId = addressId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * method queries database and compares customer ID from customer table
     * to customer ID from address table to retrieve the correct address
     * @return
     * @throws SQLException
     */
    public String getAddress() throws SQLException { return customerData.getCustomerAddress(this.customerId); }

    /**
     * method queries database and compares customer ID from customer table
     * to customer ID from address table to retrieve the correct phone number
     * @return
     * @throws SQLException
     */
    public String getPhone() throws SQLException { return customerData.getCustomerPhone(this.customerId); }

    /**
     * method queries database and compares customer ID from customer table
     * to customer ID from appointment table to retrieve the correct appointment start time
     * @return
     * @throws SQLException
     */
    public String getAppointmentTime() throws SQLException { return customerData.getCustomerAppointmentStart(this.customerId); }

    /**
     * returns customer active status as a string
     * @return
     */
    public String getIsActiveString() { return (this.isActive == 1) ? "Yes" : "No"; }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getAddressId() { return this.addressId; }
}
