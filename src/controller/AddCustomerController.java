package controller;

import dao.CityDAO;
import dao.ConnectionHandler;
import dao.CustomerDAO;
import dao.StatementHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    WindowManager window = new WindowManager();
    private String currentCity;
    private String fullPhone;
    private int currentCityId;

    /**
     * sets variables for the text fields and
     */
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private ChoiceBox <String> cityDropbox = new ChoiceBox<>();
    @FXML private TextField zip;
    @FXML private TextField country;
    @FXML private TextField phone;

    @FXML private Button saveButton;
    /**
     * handles save button click
     * @param event
     * @throws SQLException
     */
    public void setSaveClicked(ActionEvent event) {
        System.out.println("save button clicked");
        String name = this.name.getText();
        String address = this.address.getText();
        String zip = this.zip.getText();


        try {
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

            /**
             * validates input from user then calls method to insert record into the database
             * returns user to manage customers window
             */
            if (CustomerDAO.isValidCustomerInput(name, address, zip)) {
                CustomerDAO.addCustomer(name, address, currentCityId, zip, fullPhone);
                window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
            }
        } catch (SQLException s) {
            s.getStackTrace();
        } catch (InvalidValue i){
            PopupHandlers.errorAlert(2, "Invalid phone. Numbers only. Include area code");
        }

    }

    /**
     * handles cancel button click
     * issues confirmation prompt and, if confirmed, returns to manage customers screen
     * @param event
     */
    public void setCancelClicked(ActionEvent event){
        if (PopupHandlers.confirmationAlert("quit and discard unsaved changes"))
            window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
    }

    /**
     * handles user making a selection in the dropdown box
     * finds chose city's ID and uses that to populate the respective country
     * @throws SQLException
     */
    public void setCity() throws SQLException {
        currentCity = cityDropbox.getValue();
        System.out.println("city selected: " + currentCity);

        String sqlStatement = "select c.cityId, c.city, cntry.countryId, cntry.country from city c\n" +
                        "join country cntry on c.countryId = cntry.countryId;";
        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            if (rs.getString("city").equals(currentCity)) {
                currentCityId = rs.getInt("cityId");
                setCountry(rs.getString("country"));
            }
        }
        // enables the save button once a city is chosen
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
        cityDropbox.setItems(CityDAO.getCityNames());
    }
}
