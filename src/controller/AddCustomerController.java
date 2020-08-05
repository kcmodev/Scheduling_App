package controller;

import javafx.event.ActionEvent;

public class AddCustomerController {
    WindowManager window = new WindowManager();

    public void setSaveClicked(){
        System.out.println("save button clicked");
    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");
        window.windowController(event, "/gui/Appointments.fxml", WindowManager.APPOINTMENT_WINDOW_TITLE);
    }
}
