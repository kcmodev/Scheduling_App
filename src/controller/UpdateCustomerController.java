/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package controller;

import ErrorHandling.InvalidInput;
import ErrorHandling.PopupHandlers;
import dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Customer;

import java.sql.SQLException;

public class UpdateCustomerController {
    private static final WindowManager window = new WindowManager();
    private static final PopupHandlers popups = new PopupHandlers();
    private static final CityDAO cityData = new CityDAO();
    private static final CustomerDAO customerData = new CustomerDAO();
    private static Customer temp;

    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private ChoiceBox<String> city;
    @FXML
    private TextField zip;
    @FXML
    private TextField country;
    @FXML
    private TextField phone;

    /**
     * handles save button clicks
     * @param event
     */
    public void setSaveClicked(ActionEvent event) {
        try {
            String fullPhone;
            String newName = name.getText();
            String newAddress = address.getText();
            String newZip = zip.getText();
            int newCityId = cityData.getCityId(city.getValue());

            /**
             * separates phone number to 3 parts
             * then concats them together with the dashes for the correct syntax
             */
            if (phone.getText().matches("^[0-9]*$") && phone.getLength() == 10) {
                String temp1 = phone.getText().substring(0, 3);
                String temp2 = phone.getText().substring(3, 6);
                String temp3 = phone.getText().substring(6, 10);
                fullPhone = temp1 + "-" + temp2 + "-" + temp3;
            } else {
                throw new InvalidInput("Invalid phone. Numbers only. Include area code");
            }

            /**
             * validates input from user then calls method to insert record into the database
             * returns user to manage customers window
             */
            if (customerData.isValidCustomerInput(newName, newAddress, newZip)) {
                customerData.updateCustomer(temp.getCustomerId(), newName, newAddress, newZip, newCityId, fullPhone);
                window.windowController(event, "/gui/ManageCustomers.fxml", window.MANAGE_CUSTOMERS_TITLE);
            }

        } catch (InvalidInput i) {
            popups.errorAlert(2, i.getLocalizedMessage());

        } catch (SQLException s){
            s.printStackTrace();
        }
    }

    /**
     * handles cancel button clicks
     * @param event
     */
    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes")){
            window.windowController(event, "/gui/ManageCustomers.fxml", window.MANAGE_CUSTOMERS_TITLE);
        }
    }

    /**
     * uses the selected city combo box to determine the proper country to display
     * and associate with the saved record
     * @throws SQLException
     */
    public void setCity() throws SQLException {
        setCountry(CountryDAO.getCountryName(cityData.getCityId(city.getValue())));
    }

    /**
     * sets the proper country in it's text field based on the selected city
     * @param countryName
     */
    public void setCountry(String countryName) { country.setText(countryName); }

    public void setTextFields(Customer customer) throws SQLException {
        temp = customer; // save initial incoming information to compare before updating

        /**
         * sets initial values for all text fields
         */
        name.setText(customer.getName());
        address.setText(customerData.getCustomerAddress(customer.getCustomerId()));
        city.setItems(cityData.getCityNames());
        city.setValue(customerData.getCustomerCity(customer.getCustomerId()));
        setCity(); // sets country initial value
        zip.setText(customerData.getCustomerZip(customer.getCustomerId()));

        /**
         * parses incoming phone number to remove dashes
         */
        String parsedPhone = customerData.getCustomerPhone(customer.getCustomerId());
        String newParsedPhone = parsedPhone.substring(0,3) + parsedPhone.substring(4,7) + parsedPhone.substring(8,12);
        phone.setText(newParsedPhone);
    }
}
