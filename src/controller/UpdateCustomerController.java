package controller;

import dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Customer;
import org.omg.CORBA.DynAnyPackage.InvalidSeq;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateCustomerController {
    WindowManager window = new WindowManager();
    private String fullPhone;
    Customer temp;

    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private ChoiceBox<String> city = new ChoiceBox<>();
    @FXML private TextField zip;
    @FXML private TextField country;
    @FXML private TextField phone;

    public void setSaveClicked(ActionEvent event) throws SQLException {
        String newName = name.getText();
        String newAddress = address.getText();
        String newZip = zip.getText();
        String newPhone = phone.getText();
        int newCityId = CityDAO.getCityId(city.getValue());

        boolean diffName = true,
                diffAddr = true,
                diffCity = true,
                diffZip = true,
                diffPhone = true;

        System.out.println("save button clicked");

        try {
            /**
             * checks if any values have changed
             * throws error if no changes detected
             * otherwise updates current entry in the database
             */
            if (temp.getName().equals(newName))
                diffName = false;
            if (CustomerDAO.getCustomerAddress(temp.getCustomerId()).equals(newAddress))
                diffAddr = false;
            if (CustomerDAO.getCustomerCity(temp.getCustomerId()).equals(city.getValue()))
                diffCity = false;
            if (CustomerDAO.getCustomerZip(temp.getCustomerId()).equals(newZip))
                diffZip = false;
            if (CustomerDAO.getCustomerPhone(temp.getCustomerId()).equals(newPhone))
                diffPhone = false;

            /**
             * checks for no changes in any field, else runs the update customer method
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
                if (CustomerDAO.isValidCustomerInput(newName, newAddress, newZip)) {
                    CustomerDAO.updateCustomer(temp.getCustomerId(), newName, newAddress, newZip, newCityId, fullPhone);
                    window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
                }
            }

        } catch (InvalidValue e){
            PopupHandlers.errorAlert(2, "No changes detected");
        } catch (InvalidSeq q) {
            PopupHandlers.errorAlert(2, "Invalid phone. Numbers only. Include area code");
        } catch (SQLException s){
            s.printStackTrace();
        }

    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");

        if (PopupHandlers.confirmationAlert("quit and discard unsaved changes")){
            window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
        }
    }

    public void setCity() throws SQLException { setCountry(CountryDAO.getCountryName(CityDAO.getCityId(city.getValue()))); }

    public void setCountry(String countryName) { country.setText(countryName); }

    public void setTextFields(Customer customer) throws SQLException {
        temp = customer; // save initial incoming information to compare before updating

        /**
         * sets initial values for all text fields
         */
        name.setText(customer.getName());
        address.setText(CustomerDAO.getCustomerAddress(customer.getCustomerId()));
        city.setItems(CityDAO.getCityNames());
        city.setValue(CustomerDAO.getCustomerCity(customer.getCustomerId()));
        setCity(); // sets country initial value
        zip.setText(CustomerDAO.getCustomerZip(customer.getCustomerId()));

        /**
         * parses incoming phone number to remove dashes
         */
        String parsedPhone = CustomerDAO.getCustomerPhone(customer.getCustomerId());
        String newParsedPhone = parsedPhone.substring(0,3) + parsedPhone.substring(4,7) + parsedPhone.substring(8,12);
        phone.setText(newParsedPhone);
    }
}
