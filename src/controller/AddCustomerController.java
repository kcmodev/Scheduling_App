/**
 * Author: kcmodev
 * Email: *****@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package controller;

import ErrorHandling.InvalidCustomerData;
import ErrorHandling.PopupHandlers;
import dao.CityDAO;
import dao.CountryDAO;
import dao.CustomerDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    private static final WindowManager window = new WindowManager();
    private static final PopupHandlers popups = new PopupHandlers();
    private static final CityDAO cityData = new CityDAO();
    private static final CountryDAO countryData = new CountryDAO();
    private static final CustomerDAO customerData = new CustomerDAO();

    private int currentCityId;

    /**
     * sets variables for the text fields and
     */
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private ComboBox<String> cityDropbox;
    @FXML
    private TextField zip;
    @FXML
    private TextField country;
    @FXML
    private TextField phone;
    @FXML
    private Button saveButton;

    /**
     * handles save button click
     * @param event
     * @throws SQLException
     */
    public void setSaveClicked(ActionEvent event) {
        String name = this.name.getText();
        String address = this.address.getText();
        String zip = this.zip.getText();

        try {
            /**
             * separated phone number to 3 parts
             * then concats them together with the dashes for the correct syntax
             */
            String fullPhone;
            if (phone.getText().matches("^[0-9]*$") && phone.getLength() == 10) {
                String temp1 = phone.getText().substring(0, 3);
                String temp2 = phone.getText().substring(3, 6);
                String temp3 = phone.getText().substring(6, 10);
                fullPhone = temp1 + "-" + temp2 + "-" + temp3;
            } else {
                throw new InvalidCustomerData("Invalid phone. Digits only. Include area code");
            }

            /**
             * validates input from user & that customer with the same name isn't present in the database
             * calls method to insert record into the database
             * returns user to manage customers window
             */
            if (customerData.isValidCustomerInput(name, address, zip) && !customerData.customerExists(name)) {
                customerData.addCustomer(name, address, currentCityId, zip, fullPhone);
                window.windowController(event, "/gui/ManageCustomers.fxml", window.MANAGE_CUSTOMERS_TITLE);
            }

        } catch (SQLException s) {
            s.getStackTrace();
        } catch (InvalidCustomerData i){
            popups.errorAlert(2, i.getMessage());
        }
    }

    /**
     * handles cancel button click
     * issues confirmation prompt and, if confirmed, returns to manage customers screen
     * @param event
     */
    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes"))
            window.windowController(event, "/gui/ManageCustomers.fxml", window.MANAGE_CUSTOMERS_TITLE);
    }

    /**
     * handles user making a selection in the dropdown box
     * finds chose city's ID and uses that to populate the respective country
     * @throws SQLException
     */
    public void citySelected() throws SQLException {
        String currentCity = cityDropbox.getValue();
        currentCityId = cityData.getCityId(currentCity);

        /**
         * sets country name based on city ID
         */
        setCountry(countryData.getCountryName(currentCityId));

        /**
         * enables the save button once a city is chosen
         */
        saveButton.setDisable(false);
    }

    /**
     * method takes input from setCity to fill the country field based on cityId
     * @param countryName
     */
    public void setCountry(String countryName){ country.setText(countryName); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**
         * populates drop box with list of strings of all states in the database
         * available as choices to the user
         */
        cityDropbox.setItems(cityData.getCityNames());
    }
}
