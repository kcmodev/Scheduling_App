package controller;

import ErrorHandling.InvalidInput;
import ErrorHandling.PopupHandlers;
import dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Customer;
import org.omg.CORBA.DynAnyPackage.InvalidSeq;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.sql.SQLException;

public class UpdateCustomerController {
    private WindowManager window = new WindowManager();
    private PopupHandlers popups = new PopupHandlers();
    private static Customer temp;

    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private ChoiceBox<String> city = new ChoiceBox<>();
    @FXML
    private TextField zip;
    @FXML
    private TextField country;
    @FXML
    private TextField phone;

    public void setSaveClicked(ActionEvent event) throws SQLException {
        CityDAO cityData = new CityDAO();
        CustomerDAO customerData = new CustomerDAO();

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

    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes")){
            window.windowController(event, "/gui/ManageCustomers.fxml", window.MANAGE_CUSTOMERS_TITLE);
        }
    }

    public void setCity() throws SQLException {
        CityDAO cityData = new CityDAO();
        setCountry(CountryDAO.getCountryName(cityData.getCityId(city.getValue())));
    }

    public void setCountry(String countryName) { country.setText(countryName); }

    public void setTextFields(Customer customer) throws SQLException {
        CustomerDAO customerData = new CustomerDAO();
        CityDAO cityData = new CityDAO();
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
