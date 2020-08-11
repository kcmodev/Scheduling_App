package controller;

import dao.AppointmentDAO;
import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    private WindowManager window = new WindowManager();
    private PopupHandlers popups = new PopupHandlers();
    private CustomerDAO customerData = new CustomerDAO();
    private AppointmentDAO appointmentData = new AppointmentDAO();

    @FXML private ChoiceBox<String> names;
    @FXML private TextField address;
    @FXML private TextField phone;
    @FXML private ChoiceBox<String> hours;
    @FXML private ChoiceBox<String> minutes;
    @FXML private ChoiceBox<String> years;
    @FXML private ChoiceBox<String> months;
    @FXML private ChoiceBox<String> days;

    public void setSaveClicked(ActionEvent event) {
        System.out.println("save button clicked");


    }

    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }

    public void setNames() throws SQLException {
        System.out.println("setNames running");
    }

    public void onHours(){
    }
    public void onMinutes(){}

    public void onYears(){}
    public void onMonths(){}
    public void onDays(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            names.setItems(customerData.getAllCustomerNames());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
