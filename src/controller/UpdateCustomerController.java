package controller;

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
    Customer temp;

    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private ChoiceBox<String> city = new ChoiceBox<>();
    @FXML private TextField zip;
    @FXML private TextField country;
    @FXML private TextField phone;

    public void setSaveClicked(ActionEvent event) throws SQLException {
        CityDAO cityData = new CityDAO();
        CustomerDAO customerData = new CustomerDAO();

        String fullPhone;
        boolean diffName = true, diffAddr = true, diffCity = true, diffZip = true, diffPhone = true;
        String newName = name.getText();
        String newAddress = address.getText();
        String newZip = zip.getText();
        String newPhone = phone.getText();
        int newCityId = cityData.getCityId(city.getValue());

        try {
            /**
             * checks if any values have changed
             * throws error if no changes detected
             * otherwise updates current entry in the database
             */
            if (temp.getName().equals(newName))
                diffName = false;
            if (customerData.getCustomerAddress(temp.getCustomerId()).equals(newAddress))
                diffAddr = false;
            if (customerData.getCustomerCity(temp.getCustomerId()).equals(city.getValue()))
                diffCity = false;
            if (customerData.getCustomerZip(temp.getCustomerId()).equals(newZip))
                diffZip = false;
            if (customerData.getCustomerPhone(temp.getCustomerId()).equals(newPhone))
                diffPhone = false;

            /**
             * checks for no changes in all field, else runs the update customer method
             */
            if (!diffName && !diffAddr && !diffCity && !diffZip && !diffPhone) {
                throw new InvalidValue();
            } else {
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
                    throw new InvalidSeq();
                }

                /**
                 * validates input from user then calls method to insert record into the database
                 * returns user to manage customers window
                 */
                if (customerData.isValidCustomerInput(newName, newAddress, newZip)) {
                    customerData.updateCustomer(temp.getCustomerId(), newName, newAddress, newZip, newCityId, fullPhone);
                    window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
                }
            }

        } catch (InvalidValue e){
            popups.errorAlert(2, "No changes detected");
        } catch (InvalidSeq q) {
            popups.errorAlert(2, "Invalid phone. Numbers only. Include area code");
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
