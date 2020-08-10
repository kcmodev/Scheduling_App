package dao;

import controller.PopupHandlers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Address;
import models.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static String sqlStatement;
    private static ResultSet rs;

    /**
     * adds new customer to database
     * @param name
     * @param address
     * @param cityId
     * @param zip
     * @param phone
     * @throws SQLException
     */
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

    /**
     * method removes customer from database
     * @param customer
     * @throws SQLException
     */
    public static void deleteCustomer (Customer customer) throws SQLException {
        sqlStatement = "delete from customer where customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customer.getCustomerId());
        StatementHandler.getPreparedStatement().execute();
    }

    /**
     * method updates database entry for selected customer
     * @param customerId
     * @param name
     * @param address
     * @param zip
     * @param cityId
     * @param phone
     * @throws SQLException
     */
    public static void updateCustomer(int customerId, String name, String address, String zip, int cityId, String phone) throws SQLException {
        // check if address is unique
//        int addressId = AddressDAO.getAddressId(address);
//        if (AddressDAO.isNewAddress(address, cityId, zip, phone)) {
//            AddressDAO.addNewAddress(address, cityId, zip, phone);
//            addressId = AddressDAO.getAddressId(address);
//            setCustomerAddressId(customerId, addressId);
//        }

        /**
         * joins customer and address with the id for both
         * updates accordingly
         */
        sqlStatement = "update customer, address a\n" +
                "join customer c on a.addressId = c.addressId\n" +
                "set c.customerName = ?,\n" +
                " a.address = ?,\n" +
                " a.postalCode = ?,\n" +
                " a.cityId = ?,\n" +
                " a.phone = ?\n" +
                "where c.customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, name);
        StatementHandler.getPreparedStatement().setString(2, address);
        StatementHandler.getPreparedStatement().setString(3, zip);
        StatementHandler.getPreparedStatement().setInt(4, cityId);
        StatementHandler.getPreparedStatement().setString(5, phone);
        StatementHandler.getPreparedStatement().setInt(6, customerId);

        StatementHandler.getPreparedStatement().execute();
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

    public static void setCustomerAddressId(int customerId, int addressId) {
        sqlStatement = "";

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

    /**
     * returns observable list of customer objects
     * @return
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        buildCustomerData();
        return allCustomers;
    }

}
