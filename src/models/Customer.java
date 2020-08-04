package models;

import dao.ConnectionHandler;
import dao.StatementHandler;

import java.sql.*;

public class Customer{
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
//        Connection connection = ConnectionHandler.startConnection();
        StatementHandler.setPreparedStatement(ConnectionHandler.startConnection(), "SELECT addressId, address FROM address;");
//        PreparedStatement ps = StatementHandler.getPreparedStatement();
        ResultSet addressList = StatementHandler.getPreparedStatement().executeQuery();

        while (addressList.next()){
            System.out.println("ID of address being checked: " + addressList.getInt("addressId") + " | looking for: " + this.addressId);
            if (this.addressId == addressList.getInt(1)){
                System.out.println("found the matched ID, returning \"" + addressList.getString("address") + "\"");
                return addressList.getString("address");
            }
        }
        return "";
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getAddressId() { return this.addressId; }

    /**
     * method queries database and compares customer ID from customer table
     * to customer ID from address table to retrieve the correct phone number
     * @return
     * @throws SQLException
     */
    public String getPhone() throws SQLException {
        StatementHandler.setPreparedStatement(ConnectionHandler.startConnection(), "SELECT addressId, phone FROM address;");
        ResultSet phoneList = StatementHandler.getPreparedStatement().executeQuery();

        while (phoneList.next()){
            System.out.println("ID of address being checked: " + phoneList.getInt("addressId") + " | looking for: " + this.addressId);
            if (this.addressId == phoneList.getInt(1)){
                System.out.println("found the matched ID, returning \"" + phoneList.getString("phone") + "\"");
                return phoneList.getString("phone");
            }
        }
        return "";
    }

    /**
     * method queries database and compares customer ID from customer table
     * to customer ID from appointment table to retrieve the correct appointment start time
     * @return
     * @throws SQLException
     */
    public String getAppointmentTime() throws SQLException {
        StatementHandler.setPreparedStatement(ConnectionHandler.startConnection(),
                                                                 "SELECT c.customerId, a.start " +
                                                                            "FROM appointment a " +
                                                                            "RIGHT JOIN customer c on c.customerId = a.customerId;");
        ResultSet apptList = StatementHandler.getPreparedStatement().executeQuery();

        while (apptList.next()){
            System.out.println("ID of customer being checked: " + apptList.getInt("customerId") + " | looking for: " + this.customerId);
            if (this.customerId == apptList.getInt(1)) {
                System.out.println("found the matched ID, returning \"" + apptList.getString("start") + "\"");
                return apptList.getString("start");
            }
        }
        return "";
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

}
