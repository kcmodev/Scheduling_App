package controller;

import dao.ConnectionHandler;
import dao.CustomerDAO;

import dao.StatementHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Customer;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AppointmentsController implements Initializable {
    private String sqlStatement;
    WindowManager window = new WindowManager();

    @FXML private Label appointmentLabel;

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, String> customerPhoneCol;
    @FXML private TableColumn<Customer, Time> appointmentTimeCol;

    @FXML private ToggleGroup filterSelection;
    @FXML private RadioButton all;
    @FXML private RadioButton week;
    @FXML private RadioButton month;


    /**
     * method dynamically sets label to the logged in user's username
     * @param label
     */
    public void setLabel(String label) {
        System.out.println("makes it to set label with: \"" + label + "\"");
        appointmentLabel.setText(label);
    }

    public void setAddClicked(ActionEvent event){
        System.out.println("add button clicked");
        window.windowController(event, "/gui/AddCustomer.fxml", WindowManager.ADD_CUSTOMER_TITLE);
    }

    public void setUpdateClicked(ActionEvent event){
        System.out.println("update button clicked");
    }

    /**
     * method handles delete button being clicked
     * removes selected customer from the database
     * returns an error if null
     */
    public void deleteClicked () {
        System.out.println("delete button clicked");

        try {
            String name = customerTableView.getSelectionModel().getSelectedItem().getName();
            sqlStatement = "SELECT customerName FROM customer\n" +
                    "WHERE customerName = ?;";
            System.out.println("attempting to delete: \"" + name + "\"");
            System.out.println("sql statement being passed in: " + sqlStatement);

            StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
            StatementHandler.getPreparedStatement().setString(1, name);
            ResultSet set = StatementHandler.getPreparedStatement().executeQuery();

            if (set.next()) {
                sqlStatement = "DELETE FROM customer WHERE customerName = ?;";
                System.out.println("new sql statement: \"" + sqlStatement + "\"");
                StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
                StatementHandler.getPreparedStatement().setString(1, name);
//                StatementHandler.getPreparedStatement().execute();

                for (Customer customer : CustomerDAO.getAllCustomers()){
                    if (customer.getName().equals(name)){
                        System.out.println("deleting: " + customer.getName());
                        CustomerDAO.deleteCustomer(customer);
                        break;
                    }
                }
            }

            setFilterSelection();
            customerTableView.refresh();
            customerTableView.getSelectionModel().clearSelection();

        } catch (NullPointerException | SQLException e) {
            PopupHandlers.errorAlert(1, "You Must make a selection.");
        }
    }

    public void setLogOutClicked(ActionEvent event) {
        if (PopupHandlers.confirmationAlert("log out")){
            window.windowController(event, "/gui/Login.fxml", WindowManager.LOGIN_SCREEN_TITLE);
        }
    }

    public void setFilterSelection(){
        System.out.println("filtering selection");
        if (all.isSelected())
            setViewAll();
        if (week.isSelected())
            setViewWeek();
        if (month.isSelected())
            setViewMonth();
    }

    public void setViewAll() {
        System.out.println("filter set to \"view all\"");
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        appointmentTimeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));

        customerTableView.setItems(CustomerDAO.getAllCustomers());
    }

    public void setViewWeek(){
        System.out.println("filter set to \"filter by week\"");
    }

    public void setViewMonth(){
        System.out.println("filter set to \"filter by month\"");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLabel("Schedule for /ADMIN USER/");
        setViewAll();
    }
}
