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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {
    public static final String APPOINTMENT_WINDOW_TITLE = "Main Screen";
    private String sqlStatement;

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

    /**
     * method handles delete buttong being clicked
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
                set.deleteRow();
                for (Customer customer : CustomerDAO.getAllCustomers()){
                    if (customer.getName().equals(name)){
                        CustomerDAO.deleteCustomer(customer);
                        break;
                    }
                }
            }

            setFilterSelection();

        } catch (NullPointerException | SQLException e) {
            e.printStackTrace();
            PopupHandlers.errorAlert(1, "catching a weird error");
        }
    }

    public void setUpdateClicked(ActionEvent event){
        System.out.println("update button clicked");
    }

    public void setAddClicked(ActionEvent event){
        System.out.println("add button clicked");
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
