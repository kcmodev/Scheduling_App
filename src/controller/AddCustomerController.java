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

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    WindowManager window = new WindowManager();
    private String sqlStatement;
    private String currentCity;
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
    public void setSaveClicked(ActionEvent event) throws SQLException {
        System.out.println("save button clicked");
        String name = this.name.getText();
        String address = this.address.getText();
        String zip = this.zip.getText();

        /**
         * separated phone number to 3 parts
         * then concats them together with the dashes for the correct syntax
         */
        String temp1 = this.phone.getText().substring(0,3);
        String temp2 = this.phone.getText().substring(3,6);
        String temp3 = this.phone.getText().substring(6,10);
        String phone = temp1 + "-" + temp2 + "-" + temp3;


        /**
         * validates input from user then calls method to insert record into the database
         * returns user to manage customers window
         */
        if (CustomerDAO.validateCustomer(name, address, zip, phone)) {
            CustomerDAO.addCustomer(name, address, currentCityId, zip, phone);
            window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
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

        sqlStatement = "select c.cityId, c.city, cntry.countryId, cntry.country from city c\n" +
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
     * @param countryId
     */
    public void setCountry(String countryId){ country.setText(countryId); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**
         * populates drop box with list of strings of all states in the database
         * available as choices to the user
         */
        cityDropbox.setItems(CityDAO.getCityNames());
    }
}
