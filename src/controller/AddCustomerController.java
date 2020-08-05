package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddCustomerController {
    WindowManager window = new WindowManager();

    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField city;
    @FXML private TextField state;
    @FXML private TextField zip;
    @FXML private TextField country;
    @FXML private TextField phone;

    public void setSaveClicked() {
        System.out.println("save button clicked");
        String name = this.name.getText();
        String address = this.address.getText();
        String city = this.city.getText();
        String state = this.state.getText();
        String zip = this.zip.getText();
        String country = this.country.getText();
        String phone = this.phone.getText();


        if (!isValidInput(name)) {
            PopupHandlers.errorAlert(2, "Invalid name");
        }else if (!address.matches("^[0-9a-zA-Z_ ]*$")){
            PopupHandlers.errorAlert(2, "Invalid address");
        }else if (!isValidInput(city)) {
            PopupHandlers.errorAlert(2, "Invalid city");
        }else if (!isValidInput(state)){
            PopupHandlers.errorAlert(2, "Invalid state");
        }else if (!zip.matches("^[0-9]*$")) {
            PopupHandlers.errorAlert(2, "Invalid zip");
        }else if (zip.length() > 5 || zip.isEmpty()){
            PopupHandlers.errorAlert(2, "Valid zip code is 5 digits or less");
        }else if (!isValidInput(country)) {
            PopupHandlers.errorAlert(2, "Invalid country");
        }else if (!phone.matches("^[0-9]*$")) {
            PopupHandlers.errorAlert(2, "Invalid phone number. Numbers only");
        } else if (phone.length() != 10){
            PopupHandlers.errorAlert(2, "Phone number must be 10 digits. Numbers only.");
        }
        System.out.println("input successfully validated");

    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");
        window.windowController(event, "/gui/Appointments.fxml", WindowManager.APPOINTMENT_WINDOW_TITLE);
    }

    public boolean isValidInput(String input){
        if (input.matches("^[a-zA-Z0-9 _]*$") && !input.isEmpty()){ return true; }
        return false;
    }
}
