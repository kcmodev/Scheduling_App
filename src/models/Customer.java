package models;

import dao.ConnectionHandler;
import dao.StatementHandler;

import java.sql.*;

public class Customer{
    private String name;
    private int customerId;
    private int addressId;
    private int isActive;
    private String sqlStatement;

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
        return customerId;
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
    public String getAddress() throws SQLException {
        sqlStatement = "SELECT c.customerId, c.customerName, a.address FROM customer c\n" +
                         "JOIN address a on a.addressId = c.addressId\n" +
                         "WHERE c.customerId = ?;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, this.customerId);
        ResultSet set = StatementHandler.getPreparedStatement().executeQuery();

        if (set.next())
            return set.getString("address");

        return "";
    }

    /**
     * method queries database and compares customer ID from customer table
     * to customer ID from address table to retrieve the correct phone number
     * @return
     * @throws SQLException
     */
    public String getPhone() throws SQLException {
        sqlStatement = "SELECT c.customerId, a.phone from customer c\n" +
                        "JOIN address a on c.addressId = a.addressId\n" +
                        "WHERE c.customerId = ?;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, this.customerId);
        ResultSet set = StatementHandler.getPreparedStatement().executeQuery();

        if (set.next())
            return set.getString("phone");

        return "";
    }

    /**
     * method queries database and compares customer ID from customer table
     * to customer ID from appointment table to retrieve the correct appointment start time
     * @return
     * @throws SQLException
     */
    public String getAppointmentTime() throws SQLException {
        sqlStatement = "SELECT c.customerId, app.start FROM customer c\n" +
                            "JOIN appointment app ON c.customerId = app.customerId\n" +
                            "WHERE c.customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, this.customerId);
        ResultSet set = StatementHandler.getPreparedStatement().executeQuery();

        if (set.next())
            return set.getString("start");

        return "";
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getAddressId() { return this.addressId; }
}
