package controller;

import dao.CustomerDAO;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Customer;

import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {
    public static final String APPOINTMENT_WINDOW_TITLE = "Main Screen";

    @FXML
    private Label appointmentLabel;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Integer> customerIDCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private TableColumn<Customer, String> customerAddressCol;

    @FXML
    private TableColumn<Customer, String> customerPhoneCol;

    @FXML
    private TableColumn<Customer, Time> appointmentTimeCol;


    public void setLabel(String label) {
        System.out.println("makes it to set label with: \"" + label + "\"");
        appointmentLabel.setText(label);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLabel("Schedule for /ADMIN USER/");
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        appointmentTimeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        customerTableView.setItems(CustomerDAO.getAllCustomers());
    }
}
