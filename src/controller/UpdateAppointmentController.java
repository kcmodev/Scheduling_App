package controller;

import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Appointment;


public class UpdateAppointmentController {
    private WindowManager window = new WindowManager();
    private PopupHandlers popups = new PopupHandlers();

    @FXML private TextField names;
    @FXML private TextField address;
    @FXML private TextField phone;
    @FXML private ChoiceBox<String> hours;
    @FXML private ChoiceBox<String> minutes;
    @FXML private ChoiceBox<String> years;
    @FXML private ChoiceBox<String> months;
    @FXML private ChoiceBox<String> days;
    @FXML private TextField type;

    public void setSaveClicked(ActionEvent event){
    }

    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }

    public void onMonths(){

    }

    public void setTextFields(Appointment appointment){
        names.setText(appointment.getName());
        address.setText(appointment.getAddress());
        phone.setText(appointment.getPhone());

//        hours.setValue(appointment.getStartTime());
//        minutes.setValue(appointment.getStartDate());

        type.setText(appointment.getType());
    }
}
