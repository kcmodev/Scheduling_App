package dao;

import controller.PopupHandlers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Customer;

import java.sql.*;

public class CustomerDAO {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<String> allCustomerNames = FXCollections.observableArrayList();
    private static final Connection conn = ConnectionHandler.startConnection();
    private PopupHandlers popups = new PopupHandlers();
    private AddressDAO addressData = new AddressDAO();

    /**
     * adds new customer to database
     * @param name
     * @param address
     * @param cityId
     * @param zip
     * @param phone
     * @throws SQLException
     */
    public void addCustomer(String name, String address, int cityId, String zip, String phone) throws SQLException {
        StatementHandler statement = new StatementHandler();

        addressData.addNewAddress(address, cityId, zip, phone);
        int addressId = addressData.getAddressId(address);
        String sqlStatement = "insert into customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy)\n" +
                "values (?, ?, 1, NOW(), 'test', 'test');";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, name);
        statement.getPreparedStatement().setInt(2, addressId);
        statement.getPreparedStatement().execute();
    }

    /**
     * method removes customer from database
     * @param customer
     * @throws SQLException
     */
    public void deleteCustomer (Customer customer) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "delete from customer where customerId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, customer.getCustomerId());
        statement.getPreparedStatement().execute();
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
    public void updateCustomer(int customerId, String name, String address, String zip, int cityId, String phone) throws SQLException {
        StatementHandler statement = new StatementHandler();

        /**
         * checks to see if address is new, if so adds it to database
         * before adding to customer
         */
        if (addressData.isNewAddress(address, cityId, zip, phone))
            addressData.addNewAddress(address, cityId, zip, phone);

        /**
         * updates existing customer info using new addressId if necessary
         */
        String sqlStatement = "UPDATE customer SET customerName = ?, addressId = ? WHERE customerId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, name);
        statement.getPreparedStatement().setInt(2, addressData.getAddressId(address));
        statement.getPreparedStatement().setInt(3, customerId);
        statement.getPreparedStatement().executeUpdate();
    }

    /**
     * retrieves customer name with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public String getCustomerName(int customerId) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select customerId, customerName from customer where customerId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

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
    public String getCustomerAddress(int customerId) throws SQLException {
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "select c.customerId, a.address from customer c\n" +
                "join address a on c.addressId = a.addressId\n" +
                "where customerId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getInt("customerId") == customerId)
                return rs.getString("address");
        }

    return "";
    }

    public String getAddressByName(String name) throws SQLException {
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "select c.customerName, a.address from customer c\n" +
                                "join address a on c.addressId = a.addressId\n" +
                                "where c.customerName = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, name);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getString("address");

        return "";
    }

    /**
     * retrieves customer phone with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public String getCustomerPhone(int customerId) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select c.customerId, a.phone from customer c\n" +
                "join address a on c.addressId = a.addressId\n" +
                "where customerId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

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
    public String getCustomerCity(int customerId) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select c.customerId, cty.city from customer c\n" +
                          "join address a on c.addressId = a.addressId\n" +
                          "join city cty on a.cityId = cty.cityId\n" +
                          "where customerId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

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
    public String getCustomerZip(int customerId) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select c.customerId, a.postalCode from address a\n" +
                         "join customer c on c.addressId = a.addressId\n" +
                         "where customerId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getString("postalCode");

    return "";
    }

    /**
     * get customerId with customer name
     * @param name
     * @return
     * @throws SQLException
     */
    public int getCustomerIdByName(String name) throws SQLException {
        StatementHandler statementHandler = new StatementHandler();

        String sqlStatement = "select customerId from customer where customerName = ?;";

        statementHandler.setPreparedStatement(conn, sqlStatement);
        statementHandler.getPreparedStatement().setString(1, name);
        ResultSet rs = statementHandler.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getInt("customerId");

        return 0;
    }

    /**
     * retrieves customer appointment start time with customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public String getCustomerAppointmentStart(int customerId) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "SELECT c.customerId, app.start FROM customer c\n" +
                "JOIN appointment app ON c.customerId = app.customerId\n" +
                "WHERE c.customerId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, customerId);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getString("start");

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
    public boolean isValidCustomerInput(String name, String address, String zip) {
        if (!isValidInput(name)) {
            popups.errorAlert(2, "Invalid name");
            return false;
        }else if (!isValidInput(address)){
            popups.errorAlert(2, "Invalid address");
            return false;
        }else if (!zip.matches("^[0-9]*$")) {
            popups.errorAlert(2, "Invalid zip");
            return false;
        }else if (zip.length() > 5 || zip.isEmpty()){
            popups.errorAlert(2, "Valid zip code is 5 digits or less");
            return false;
        }

    return true;
    }

    /**
     * method checks for customer in database with a matching name
     * @param name
     * @return
     * @throws SQLException
     */
    public boolean customerExists(String name) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select customerName from customer\n" +
                "where customerName = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, name);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        if(rs.next()){
            if (rs.getString("customerName").equals(name)) {
                popups.errorAlert(2, "Customer with that name already exists.");
                return true;
            }
        } else {
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
    public boolean isValidInput(String input){ return (input.matches("^[a-zA-Z0-9 ]*$") && !input.isEmpty()) ? true : false; }

    /**
     * builds observable list to populate customer table view
     * @throws SQLException
     */
    public static void buildCustomerData() throws SQLException {
        allCustomers.clear();

        StatementHandler statement = new StatementHandler();
        String sqlStatement = "SELECT * FROM customer";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            String name = rs.getString("customerName");
            int customerID = rs.getInt("customerID");
            int addressID = rs.getInt("addressId");
            int active = rs.getInt("active");

            Customer customer = new Customer(name, customerID, addressID, active);

            allCustomers.add(customer);
            allCustomerNames.add(name);
        }
    }

    /**
     * returns observable list of customer objects
     * @return
     * @throws SQLException
     */
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        buildCustomerData();
        return allCustomers;
    }

    /**
     * returns observable list of all customer names for choice boxes
     * @return
     */
    public ObservableList<String> getAllCustomerNames() throws SQLException {
        buildCustomerData();
        return allCustomerNames;
    }

}
