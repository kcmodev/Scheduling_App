package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();


    public static void buildCustomerData() throws SQLException {
        Connection connection = ConnectionHandler.startConnection();
        StatementHandler.setPreparedStatement(connection, "SELECT * FROM customer");
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
    }

    public static ObservableList<Customer> getAllCustomers(){ return allCustomers; }

}
