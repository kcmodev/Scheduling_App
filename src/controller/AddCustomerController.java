package controller;

import dao.ConnectionHandler;
import dao.StatementHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddCustomerController {
    WindowManager window = new WindowManager();
    private String sqlStatement;

    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField city;
    @FXML private TextField state;
    @FXML private TextField zip;
    @FXML private TextField country;
    @FXML private TextField phone;

    public void setSaveClicked() throws SQLException {
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

        /**
         * find if address is already in database
         * if so, link to current customer with the address ID
         * if not, add the address to the database and add the auto-incremented ID
         * to the customer being created
         */
        sqlStatement = "SELECT addressId, ? FROM address;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, address);
        ResultSet set = StatementHandler.getPreparedStatement().executeQuery();

//        if (!set.next()){ // result not present, create new db entry
//            sqlStatement = "";
//            StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
//            StatementHandler.getPreparedStatement().setString(1, address);
//            set = StatementHandler.getPreparedStatement().executeQuery();
//        }


    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");
        window.windowController(event, "/gui/Appointments.fxml", WindowManager.APPOINTMENT_WINDOW_TITLE);
    }

    public boolean isValidInput(String input){
        if (input.matches("^[a-zA-Z0-9 _]*$") && !input.isEmpty()){ return true; }
        return false;
    }

//    public boolean isState() {
//        final String STATES = "|AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH" +
//                "|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY|";
//    }
}
