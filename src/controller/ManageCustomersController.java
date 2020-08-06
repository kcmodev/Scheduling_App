package controller;

import javafx.event.ActionEvent;

public class ManageCustomersController {

    WindowManager window = new WindowManager();

    public void setAddClicked(ActionEvent event){
        System.out.println("add button clicked");
        window.windowController(event, "/gui/AddCustomer.fxml", WindowManager.ADD_CUSTOMER_TITLE);
    }

    public void setUpdateClicked(ActionEvent event){
        System.out.println("update button clicked");
        window.windowController(event, "/gui/UpdateCustomer.fxml", WindowManager.UPDATE_CUSTOMER_TITLE);
    }

    public void setDeleteClicked(ActionEvent event){
        System.out.println("delete button clicked");
    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");
        window.windowController(event, "gui/ManageAppointments.fxml", WindowManager.MANAGE_APPOINTMENTS_WINDOW_TITLE);
    }
}
