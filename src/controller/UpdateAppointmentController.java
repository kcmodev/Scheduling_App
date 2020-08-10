package controller;

import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Appointment;


public class UpdateAppointmentController {
    private WindowManager window = new WindowManager();
    private PopupHandlers popups = new PopupHandlers();

    @FXML private TextField customerName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPhone;
    @FXML private TextField appointmentTime;
    @FXML private TextField appointmentDate;
    @FXML private TextField appointmentType;

    public void setSaveClicked(ActionEvent event){
        CustomerDAO customer = new CustomerDAO();
        String time = appointmentTime.getText();
        String date = appointmentDate.getText();
        String type = appointmentType.getText();

        if (time.matches("^[0-9]*$") && time.length() == 6){
            if (date.matches("^[0-9]*$") && time.length() == 8){
                String dateTime = date + time;
            } else {
                popups.errorAlert(2, "Numbers only. No slashes. Use format YYYY MM DD (no spaces, include zeroes)");
            }
        } else {
            popups.errorAlert(2, "Numbers only. Leave out semicolons. And use format HH MM SS (no spaces, include zeroes)");
        }
        if (customer.isValidInput(type)){

        }
    }

    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
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
