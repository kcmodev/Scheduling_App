package dao;

import controller.PopupHandlers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static String sqlStatement;


    public static void buildCustomerData() throws SQLException {
        allCustomers.clear();
        System.out.println("building customer object array");
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, "SELECT * FROM customer");
        PreparedStatement getCustList = StatementHandler.getPreparedStatement();
        ResultSet rs = getCustList.executeQuery();

        while (rs.next()){
            String name = rs.getString("customerName");
            int customerID = rs.getInt("customerID");
            int addressID = rs.getInt("addressId");
            int active = rs.getInt("active");

            Customer customer = new Customer(name, customerID, addressID, active);
            allCustomers.add(customer);
        }

        System.out.println("Customer List: ");
        for (Customer cust : allCustomers){
            System.out.println(cust.getName());
        }
        System.out.println();
    }

    public static void addCustomer(String name, String address, int cityId, String zip, String phone) throws SQLException {
        AddressDAO.addNewAddress(address, cityId, zip, phone);
        int addressId = AddressDAO.getAddressId(address);

        sqlStatement = "insert into customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy)\n" +
                "values (?, ?, 1, NOW(), 'test', 'test');";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, name);
        StatementHandler.getPreparedStatement().setInt(2, addressId);

        StatementHandler.getPreparedStatement().execute();
        System.out.println("customer: " + name + " added successfully");

    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        buildCustomerData();
        return allCustomers;
    }

    public static void deleteCustomer (Customer customer){ allCustomers.remove(customer); }

    public static String getCustomerName(int customerId) throws SQLException {
        String sql = "select customerId, customerName from customer where customerId = ?;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sql);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getInt("customerId") == customerId)
                return rs.getString("customerName");
        }

        return "";
    }

    public static String getCustomerAddress(int customerId) throws SQLException {
        String sql = "select c.customerId, a.address from customer c\n" +
                "join address a on c.addressId = a.addressId\n" +
                "where customerId = ?;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sql);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getInt("customerId") == customerId)
                return rs.getString("address");
        }

        return "";
    }

    public static String getCustomerPhone(int customerId) throws SQLException {
        String sql = "select c.customerId, a.phone from customer c\n" +
                "join address a on c.addressId = a.addressId\n" +
                "where customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sql);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getInt("customerId") == customerId)
                return rs.getString("phone");
        }

     return "";
    }

    public static boolean validateCustomer(String name, String address, String zip, String phone) throws SQLException {
        sqlStatement = "select customerName from customer";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        ResultSet set = StatementHandler.getPreparedStatement().executeQuery();

        /**
         * checks all input fields for valid input
         */
        if (!isValidInput(name)) {
            PopupHandlers.errorAlert(2, "Invalid name");
        }else if (!isValidInput(address)){
            PopupHandlers.errorAlert(2, "Invalid address");
        }else if (!zip.matches("^[0-9]*$")) {
            PopupHandlers.errorAlert(2, "Invalid zip");
        }else if (zip.length() > 5 || zip.isEmpty()){
            PopupHandlers.errorAlert(2, "Valid zip code is 5 digits or less");
        }else if (!phone.matches("^[0-9]*$")) {
            PopupHandlers.errorAlert(2, "Invalid phone number. Numbers only");
        }else if (phone.length() != 10){
            PopupHandlers.errorAlert(2, "Phone number must be 10 digits. Numbers only.");
        }
        System.out.println("input successfully validated");

        /**
         * checks result set for existing customer with matching name
         */
        while (set.next()){
            if (set.getString("customerName").equals(name)) {
                PopupHandlers.errorAlert(2, "Customer with that name already exists.");
                return false;
            }
            else { return true; }
        }
        return false;
    }

    public static boolean isValidInput(String input){
        if (input.matches("^[a-zA-Z0-9 _]*$") && !input.isEmpty()){ return true; }
        return false;
    }

}
