package controller;

import dao.ConnectionHandler;
import dao.StatementHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddCustomerController {
    WindowManager window = new WindowManager();
    private String sqlStatement;
    private boolean isValidCustomer = true;

    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField city;
    @FXML private TextField zip;
    @FXML private TextField country;
    @FXML private TextField phone;

    public void setSaveClicked(ActionEvent event) throws SQLException {
        System.out.println("save button clicked");
        String name = this.name.getText();
        String address = this.address.getText();
        String city = this.city.getText();
        String zip = this.zip.getText();
        String country = this.country.getText();
        String phone = this.phone.getText();

        sqlStatement = "SELECT c.customerName, c.customerId, c.addressId, a.address, a.addressId, a.cityId, a.postalCode, cty.cityId, cty.city, cty.countryId, cntry.countryId, cntry.country FROM address a\n" +
                "JOIN customer c ON a.addressId = c.addressId\n" +
                "JOIN city cty ON a.cityId = cty.cityId\n" +
                "JOIN country cntry ON cty.countryId = cntry.countryId;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        ResultSet set = StatementHandler.getPreparedStatement().executeQuery();

        /**
         * checks all input fields for valid input
         */
        if (!isValidInput(name)) {
            PopupHandlers.errorAlert(2, "Invalid name");
        }else if (!isValidInput(address)){
            PopupHandlers.errorAlert(2, "Invalid address");
        }else if (!isValidInput(city)) {
            PopupHandlers.errorAlert(2, "Invalid city");
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
         * checks result set for existing customer with matching name
         */
        while (set.next()){
           if (set.getString("customerName").equals(name)) {
               PopupHandlers.errorAlert(2, "Customer with that name already exists.");
               isValidCustomer = false;
               break;
           }
           else { isValidCustomer = true; }
        }

//        if (isValidCustomer) {
//            System.out.println("customer \"" + name + "\" being added ");
//
//            /**
//             * add country to db if duplicate doesn't exist
//             */
//            sqlStatement = ";";
//            StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
//            StatementHandler.getPreparedStatement().setString(1, country);
//            StatementHandler.getPreparedStatement().setString(2, country);
//            set = StatementHandler.getPreparedStatement().executeQuery();
//
//
//            /**
//             * add city to db if duplicate doesn't exist
//             */
//            sqlStatement ="";
//            StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
//            StatementHandler.getPreparedStatement().setString(1, city);
//            StatementHandler.getPreparedStatement().setString(2, city);
//
//            /**
//             * add customer address to db if duplicate doesn't exist
//             */
//            sqlStatement ="";
//            StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
//            StatementHandler.getPreparedStatement().setString(1, address);
//            StatementHandler.getPreparedStatement().setString(2, address);
//
//            /**
//             * add customer name to db if duplicate doesn't exist
//             */
//            sqlStatement ="";
//            StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
//            StatementHandler.getPreparedStatement().setString(1, name);
//            StatementHandler.getPreparedStatement().setString(2, name;


//            System.out.println("returning to main screen");
//            window.windowController(event, "/gui/ManageAppointments.fxml", WindowManager.APPOINTMENT_WINDOW_TITLE);
//        }

    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");

        if (PopupHandlers.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
        }
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
