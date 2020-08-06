package controller;

import javafx.event.ActionEvent;

public class UpdateAppointmentController {

    WindowManager window = new WindowManager();

    public void setSaveClicked(ActionEvent event){
        System.out.println("save button clicked");
    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");

        if (PopupHandlers.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", WindowManager.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }
}
