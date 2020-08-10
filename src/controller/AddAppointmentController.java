package controller;

import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    private WindowManager window = new WindowManager();
    private PopupHandlers popups = new PopupHandlers();

    @FXML private ChoiceBox<String> names;
    @FXML private TextField address;
    @FXML private TextField phone;
    @FXML private ChoiceBox<String> times;

    public void setSaveClicked(ActionEvent event) {
        System.out.println("save button clicked");


    }

    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }

    public void setNames() {
        CustomerDAO customer = new CustomerDAO();
        names.setItems(customer.getAllCustomerNames());
    }

    public void setTimes(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNames();
    }
}
