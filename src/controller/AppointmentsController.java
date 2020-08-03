package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {
    public static final String APPOINTMENT_WINDOW_TITLE = "Main Screen";

    @FXML
    private Label appointmentLabel;
    @FXML
    private TableColumn<Customer, String> customerIDCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;
    @FXML
    private TableColumn<Customer, String> appointmentTimeCol;



    public void setLabel(String label) {
        System.out.println("makes it to set label with: \"" + label + "\"");
        appointmentLabel.setText(label);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }
}
