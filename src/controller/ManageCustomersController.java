package controller;

import dao.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Customer;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ManageCustomersController implements Initializable {

    WindowManager window = new WindowManager();

    /**
     * sets table values
     */
    @FXML TableView <Customer> customerTableView;
    @FXML TableColumn <Customer, Integer> customerIDCol;
    @FXML TableColumn <Customer, String> customerNameCol;
    @FXML TableColumn <Customer, String> customerAddressCol;
    @FXML TableColumn <Customer, String> customerPhoneCol;
    @FXML TableColumn <Customer, Integer> isActiveStringCol;

    @FXML TextField customerSearch;


    /**
     * method handles add button click
     * @param event
     */
    public void setAddClicked(ActionEvent event){
        System.out.println("add button clicked");
        window.windowController(event, "/gui/AddCustomer.fxml", WindowManager.ADD_CUSTOMER_TITLE);
    }

    /**
     * method handles update button click
     * passes customer data to the next window for updating
     * @param event
     */
    public void setUpdateClicked(ActionEvent event){
        System.out.println("update button clicked");
        window.windowController(event, "/gui/UpdateCustomer.fxml", WindowManager.UPDATE_CUSTOMER_TITLE);
    }

    /**
     * method handles delete button click
     * calls method to remove customer from database
     * @param event
     */
    public void setDeleteClicked(ActionEvent event){
        System.out.println("delete button clicked");
    }

    /**
     * method handles cancel button click
     * @param event
     */
    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");
        window.windowController(event, "/gui/ManageAppointments.fxml", WindowManager.MANAGE_APPOINTMENTS_WINDOW_TITLE);
    }

    /**
     * method handles search button functionality
     * search results based on either name or ID
     * @param event
     */
    public void setSearchClicked(ActionEvent event) throws SQLException {
        System.out.println("search clicked");
        ObservableList<Customer> filtered = FXCollections.observableArrayList();

        System.out.println("filtered before: ");
        for (Customer c : filtered)
            System.out.println(c);

        if (customerSearch.getText().matches("^[0-9]*$") && !customerSearch.getText().isEmpty()){ // search by ID
            int id = Integer.parseInt(customerSearch.getText());

            filtered = CustomerDAO.getAllCustomers().stream()
                    .filter( x -> x.getCustomerId() == id)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(), y -> FXCollections.observableArrayList(y)));
            customerTableView.setItems(filtered);

            System.out.println("filtered after: ");
            for (Customer c : filtered)
                System.out.println(c);

        } else if (CustomerDAO.isValidInput(customerSearch.getText()) && !customerSearch.getText().isEmpty()){
            String name = customerSearch.getText();

            filtered = CustomerDAO.getAllCustomers().stream()
                    .filter( x -> x.getName().contains(name))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(), y -> FXCollections.observableArrayList(y)));
            customerTableView.setItems(filtered);

            System.out.println("filtered after: ");
            for (Customer c : filtered)
                System.out.println(c);

        } else {
            customerTableView.setItems(CustomerDAO.getAllCustomers());
        }
    }

    /**
     * method sets values for table and display properties
     * @throws SQLException
     */
    public void setTableProperties() throws SQLException {
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        isActiveStringCol.setCellValueFactory(new PropertyValueFactory<>("isActiveString"));

        customerIDCol.setStyle("-fx-alignment: CENTER;");
        customerNameCol.setStyle("-fx-alignment: CENTER;");
        customerAddressCol.setStyle("-fx-alignment: CENTER;");
        customerPhoneCol.setStyle("-fx-alignment: CENTER;");
        isActiveStringCol.setStyle("-fx-alignment: CENTER;");

        customerIDCol.setResizable(false);
        customerNameCol.setResizable(false);
        customerAddressCol.setResizable(false);
        customerPhoneCol.setResizable(false);
        isActiveStringCol.setResizable(false);

        customerTableView.setItems(CustomerDAO.getAllCustomers());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setTableProperties();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
