package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.Appointment;

import java.net.URL;
import java.util.ResourceBundle;


public class UpdateAppointmentController {
    WindowManager window = new WindowManager();

    @FXML private TextField customerName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPhone;
    @FXML private TextField appointmentTime;
    @FXML private TextField appointmentDate;
    @FXML private TextField appointmentType;

    public void setSaveClicked(ActionEvent event){
        System.out.println("save button clicked");
        

    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");

        if (PopupHandlers.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", WindowManager.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }

    public void setTextFields(Appointment appointment){
        customerName.setText(appointment.getName());
        customerAddress.setText(appointment.getAddress());
        customerPhone.setText(appointment.getPhone());
        appointmentTime.setText(appointment.getStartTime());
        appointmentDate.setText(appointment.getStartDate());
        appointmentType.setText(appointment.getType());
    }
}
