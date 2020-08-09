package dao;

import controller.PopupHandlers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

public class CustomerDAO {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static String sqlStatement;
    private static ResultSet rs;

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

    public static void deleteCustomer (Customer customer) throws SQLException {
        sqlStatement = "delete from customer where customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customer.getCustomerId());
        StatementHandler.getPreparedStatement().execute();
    }

    public static void updateCustomer(int customerId, String name, String address, String zip, int cityId, String phone) throws SQLException {
        System.out.println("customer ID: " + customerId);
        System.out.println("name: " + name);
        System.out.println("address: " + address);
        System.out.println("zip: " + zip);
        System.out.println("cityId: " + cityId);
        System.out.println("phone: " + phone);

        // retrieve address ID based on address text
        if (AddressDAO.isNewAddress(address, cityId, zip, phone))
            AddressDAO.addNewAddress(address, cityId, zip, phone);

        int addressId = AddressDAO.getAddressId(address);
        System.out.println("address ID: " + addressId);

        /**
         * update address table
         */
        sqlStatement = "update address\n" +
                "set address = ?\n" +
                "set postalCode = ?\n" +
                "set cityId = ?\n" +
                "set phone = ?\n" +
                "where addressId = ?";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, address);
        StatementHandler.getPreparedStatement().setString(2, zip);
        StatementHandler.getPreparedStatement().setInt(3, cityId);
        StatementHandler.getPreparedStatement().setString(4, phone);
        StatementHandler.getPreparedStatement().setInt(5, addressId);
        StatementHandler.getPreparedStatement().execute();

        /**
         * update customer table
         */
        sqlStatement = "update customer\n" +
                "set customerName = ?\n" +
                "where addressId = ?";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, name);
        StatementHandler.getPreparedStatement().setInt(2, addressId);

//        sqlStatement = "update customer, address a\n" +
//                "join customer c on a.addressId = c.addressId\n" +
//                "set c.customerName = ?\n" +
//                "set a.address = ?\n" +
//                "set a.postalCode = ?\n" +
//                "set a.cityId = ?\n" +
//                "set a.phone = ?\n" +
//                "where c.customerId = ?;";
//
//        System.out.println("SQL STATEMENT COMING IN:");
//        System.out.println(sqlStatement);
//
//        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
//        StatementHandler.getPreparedStatement().setString(1, name);
//        StatementHandler.getPreparedStatement().setString(2, address);
//        StatementHandler.getPreparedStatement().setString(3, zip);
//        StatementHandler.getPreparedStatement().setInt(4, cityId);
//        StatementHandler.getPreparedStatement().setString(5, phone);
//        StatementHandler.getPreparedStatement().setInt(6, customerId);
//
//        System.out.println("SQL STATEMENT GOING OUT:");
//        System.out.println(StatementHandler.getPreparedStatement().toString());
//
//        StatementHandler.getPreparedStatement().execute();
    }



    /**
     * retrieves customer name with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static String getCustomerName(int customerId) throws SQLException {
        sqlStatement = "select customerId, customerName from customer where customerId = ?;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getInt("customerId") == customerId)
                return rs.getString("customerName");
        }
        return "";
    }

    /**
     * retrieves customer address with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static String getCustomerAddress(int customerId) throws SQLException {
        sqlStatement = "select c.customerId, a.address from customer c\n" +
                "join address a on c.addressId = a.addressId\n" +
                "where customerId = ?;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getInt("customerId") == customerId)
                return rs.getString("address");
        }
        return "";
    }

    /**
     * retrieves customer phone with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static String getCustomerPhone(int customerId) throws SQLException {
        sqlStatement = "select c.customerId, a.phone from customer c\n" +
                "join address a on c.addressId = a.addressId\n" +
                "where customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getInt("customerId") == customerId)
                return rs.getString("phone");
        }
     return "";
    }

    /**
     * retrieves customer city name with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static String getCustomerCity(int customerId) throws SQLException {
        sqlStatement = "select c.customerId, cty.city from customer c\n" +
                          "join address a on c.addressId = a.addressId\n" +
                          "join city cty on a.cityId = cty.cityId\n" +
                          "where customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getString("city");
        return "";
    }

    /**
     * retrieves customer postal code with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static String getCustomerZip(int customerId) throws SQLException {
        sqlStatement = "select c.customerId, a.postalCode from address a\n" +
                         "join customer c on c.addressId = a.addressId\n" +
                         "where customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getString("postalCode");
        return "";
    }

    /**
     * retrieves customer appointment start time with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static String getCustomerAppointmentStart(int customerId) throws SQLException {
        sqlStatement = "SELECT c.customerId, app.start FROM customer c\n" +
                "JOIN appointment app ON c.customerId = app.customerId\n" +
                "WHERE c.customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        ResultSet set = StatementHandler.getPreparedStatement().executeQuery();

        if (set.next())
            return set.getString("start");
        return "";
    }

    /**
     * checks all user input for valid entries
     * @param name
     * @param address
     * @param zip
     * @return
     * @throws SQLException
     */
    public static boolean isValidCustomerInput(String name, String address, String zip) throws SQLException {
        if (!isValidInput(name)) {
            PopupHandlers.errorAlert(2, "Invalid name");
            return false;
        }else if (!isValidInput(address)){
            PopupHandlers.errorAlert(2, "Invalid address");
            return false;
        }else if (!zip.matches("^[0-9]*$")) {
            PopupHandlers.errorAlert(2, "Invalid zip");
            return false;
        }else if (zip.length() > 5 || zip.isEmpty()){
            PopupHandlers.errorAlert(2, "Valid zip code is 5 digits or less");
            return false;
        }

        System.out.println("input OK");
        return true;
    }

    public static boolean customerExists(String name) throws SQLException {
        sqlStatement = "select customerName from customer\n" +
                "where customerName = ?;";

        /**
         * checks result set for existing customer with matching name
         */
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, name);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        if(rs.next()){
            if (rs.getString("customerName").equals(name)) {
                PopupHandlers.errorAlert(2, "Customer with that name already exists.");
                return true;
            }
        } else {
            System.out.println("customer \"" + name + "\" already exists");
            return false;
        }
        return false;
    }

    /**
     * uses regex to validate input
     * alphanumeric and spaces allowed
     * @param input
     * @return
     */
    public static boolean isValidInput(String input){
        if (input.matches("^[a-zA-Z0-9 ]*$") && !input.isEmpty()){ return true; }
        return false;
    }

    /**
     * builds observable list to populate customer table view
     * @throws SQLException
     */
    public static void buildCustomerData() throws SQLException {
        allCustomers.clear();
        System.out.println("building customer object array");
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, "SELECT * FROM customer");
        PreparedStatement getCustList = StatementHandler.getPreparedStatement();
        rs = getCustList.executeQuery();

        while (rs.next()){
            String name = rs.getString("customerName");
            int customerID = rs.getInt("customerID");
            int addressID = rs.getInt("addressId");
            int active = rs.getInt("active");

            Customer customer = new Customer(name, customerID, addressID, active);
            allCustomers.add(customer);
        }
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        buildCustomerData();
        return allCustomers;
    }

}
