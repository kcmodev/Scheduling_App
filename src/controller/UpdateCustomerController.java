package controller;

import dao.CityDAO;
import dao.ConnectionHandler;
import dao.CustomerDAO;
import dao.StatementHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.City;
import models.Customer;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateCustomerController {
    WindowManager window = new WindowManager();
    private int currentCityId;
    private String currentCity;
    private String currentCountry;
    private String fullPhone;
    Customer temp;

    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private ChoiceBox<String> city = new ChoiceBox<>();
    @FXML private TextField zip;
    @FXML private TextField country;
    @FXML private TextField phone;

    public void setSaveClicked(ActionEvent event){
        String nameField = name.getText();
        String addressField = address.getText();
        String zipField = zip.getText();
        String phoneField = phone.getText();


        System.out.println("save button clicked");

        try {
            /**
             * checks if any values have changed
             * throws error if no changes detected
             * otherwise updates current entry in the database
             */
            if (temp.getName().equals(nameField))
                throw new InvalidValue();
            if (CustomerDAO.getCustomerAddress(temp.getCustomerId()).equals(addressField))
                throw new InvalidValue();
            if (CustomerDAO.getCustomerCity(temp.getCustomerId()).equals(currentCity))
                throw new InvalidValue();
            if (CustomerDAO.getCustomerZip(temp.getCustomerId()).equals(zipField))
                throw new InvalidValue();
            if (CustomerDAO.getCustomerPhone(temp.getCustomerId()).equals(phoneField))
                throw new InvalidValue();

            /**
             * separated phone number to 3 parts
             * then concats them together with the dashes for the correct syntax
             */
            if (phone.getText().matches("^[0-9]*$") && phone.getLength() == 10) {
                String temp1 = phone.getText().substring(0, 3);
                String temp2 = phone.getText().substring(3, 6);
                String temp3 = phone.getText().substring(6, 10);
                fullPhone = temp1 + "-" + temp2 + "-" + temp3;
            } else {
                throw new InvalidValue();
            }



        } catch (InvalidValue e){
            PopupHandlers.errorAlert(2, "No changes detected");
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

    public void setCity() throws SQLException {
        currentCity = city.getValue();
        System.out.println("city selected: " + currentCity);

        String sqlStatement = "select c.cityId, c.city, cntry.countryId, cntry.country from city c\n" +
                                  "join country cntry on c.countryId = cntry.countryId;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getString("city").equals(currentCity)) {
                currentCityId = rs.getInt("cityId");
                setCountry(rs.getString("country"));
                currentCountry = country.getText();
            }
        }
    }

    public void setCountry(String countryName) { country.setText(countryName); }

    public void setTextFields(Customer customer) throws SQLException {
        temp = customer; // save initial incoming information to compare before updating

        name.setText(customer.getName());
        address.setText(CustomerDAO.getCustomerAddress(customer.getCustomerId()));
        city.setItems(CityDAO.getCityNames());
        city.setValue(CustomerDAO.getCustomerCity(customer.getCustomerId()));
        setCity(); // sets country initial value
        zip.setText(CustomerDAO.getCustomerZip(customer.getCustomerId()));

        String parsedPhone = CustomerDAO.getCustomerPhone(customer.getCustomerId());
        parsedPhone = parsedPhone.substring(0,2) + parsedPhone.substring(4,6) + parsedPhone.substring(8,10);
        phone.setText(parsedPhone);
    }
}
