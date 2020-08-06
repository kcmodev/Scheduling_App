package controller;

import javafx.event.ActionEvent;

public class AddAppointmentController {

    WindowManager window = new WindowManager();

    public void setSaveClicked() {

    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");
        window.windowController(event, "/gui/Appointments.fxml", WindowManager.APPOINTMENT_WINDOW_TITLE);
    }
}
