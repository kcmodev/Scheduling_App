package controller;

import dao.CityDAO;
import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.City;
import models.Customer;

import java.sql.SQLException;

public class UpdateCustomerController {
    WindowManager window = new WindowManager();

    @FXML
    TextField name;
    @FXML TextField address;
    @FXML ChoiceBox<City> city;
    @FXML TextField zip;
    @FXML TextField country;
    @FXML TextField phone;

    public void setSaveClicked(ActionEvent event){
        System.out.println("save button clicked");
    }

    public void setCancelClicked(ActionEvent event){
        System.out.println("cancel button clicked");

        if (PopupHandlers.confirmationAlert("quit and discard unsaved changes")){
            window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
        }
    }

    public void setCity(){

    }

    public void setTextFields(Customer customer) throws SQLException {
        name.setText(customer.getName());
        address.setText(customer.getAddress());
        //set city drop box
        zip.setText(CustomerDAO.getCustomerZip(customer.getCustomerId()));
    }
}
