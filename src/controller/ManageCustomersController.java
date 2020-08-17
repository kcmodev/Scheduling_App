/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package controller;

import ErrorHandling.PopupHandlers;
import dao.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManageCustomersController implements Initializable {
    private static final WindowManager window = new WindowManager();
    private static final PopupHandlers popups = new PopupHandlers();
    private static final CustomerDAO customerData = new CustomerDAO();

    /**
     * sets table values
     */
    @FXML
    private TableView <Customer> customerTableView;
    @FXML
    private TableColumn <Customer, Integer> customerIDCol;
    @FXML
    private TableColumn <Customer, String> customerNameCol;
    @FXML
    private TableColumn <CustomerDAO, String> customerAddressCol;
    @FXML
    private TableColumn <CustomerDAO, String> customerPhoneCol;
    @FXML
    private TableColumn <Customer, Integer> isActiveStringCol;
    @FXML
    private TextField customerSearch;

    /**
     * method handles add button click
     * @param event
     */
    public void setAddClicked(ActionEvent event){
        window.windowController(event, "/gui/AddCustomer.fxml", window.ADD_CUSTOMER_TITLE);
    }

    /**
     * method handles update button click
     * passes customer data to the next window for updating
     * @param event
     */
    public void setUpdateClicked(ActionEvent event) throws IOException {

        try {
            if (customerTableView.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gui/UpdateCustomer.fxml"));
                Parent parent = loader.load();
                Scene modPartScene = new Scene(parent);

                UpdateCustomerController controller = loader.getController();
                controller.setTextFields(customerTableView.getSelectionModel().getSelectedItem());

                Stage newWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
                newWindow.setScene(modPartScene);
                newWindow.setResizable(false);
                newWindow.setTitle(window.UPDATE_CUSTOMER_TITLE);
                newWindow.show();
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            popups.errorAlert(1, "You must choose a customer to modify");
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    /**
     * method handles delete button click
     * calls method to remove customer from database
     */
    public void setDeleteClicked(){
        try {
            Customer selected = customerTableView.getSelectionModel().getSelectedItem();
            customerData.deleteCustomer(selected);
            customerTableView.setItems(customerData.getAllCustomers());
        } catch (NullPointerException e){
            popups.errorAlert(1, "You must make a selection");
        } catch (SQLException s){
            s.printStackTrace();
        }
    }

    /**
     * method handles cancel button click
     * @param event
     */
    public void setCancelClicked(ActionEvent event){
        window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
    }

    /**
     * method handles search button functionality
     * search results based on either name or ID
     */
    public void setSearchClicked() throws SQLException {
        ObservableList<Customer> filtered;

        /**
         * uses stream and lambda to filter results and assign them
         * to the filtered ObservableList for viewing on the customer table
         */
        if (customerSearch.getText().matches("^[0-9]*$") && !customerSearch.getText().isEmpty()){ // search by ID
            int id = Integer.parseInt(customerSearch.getText());

            /**
             * this stream/lambda filters by customer ID
             */
            filtered = customerData.getAllCustomers().stream()
                    .filter( x -> x.getCustomerId() == id)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(), y -> FXCollections.observableArrayList(y)));
            customerTableView.setItems(filtered);

        } else if (customerData.isAlphanumeric(customerSearch.getText()) && !customerSearch.getText().isEmpty()){
            String name = customerSearch.getText();

            /**
             * this stream/lambda filters by customer name
             */
            filtered = customerData.getAllCustomers().stream()
                    .filter( x -> x.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(), y -> FXCollections.observableArrayList(y)));
            customerTableView.setItems(filtered);

        } else {
            customerTableView.setItems(customerData.getAllCustomers());
        }
    }

    /**
     * method sets values for table and display properties
     * @throws SQLException
     */
    private void setTableProperties() throws SQLException {
        CustomerDAO customer = new CustomerDAO();

        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        isActiveStringCol.setCellValueFactory(new PropertyValueFactory<>("isActiveString"));

        customerIDCol.setStyle("-fx-alignment: CENTER;");
        customerNameCol.setStyle("-fx-alignment: CENTER;");
        customerAddressCol.setStyle("-fx-alignment: CENTER;");
        customerPhoneCol.setStyle("-fx-alignment: CENTER;");
        isActiveStringCol.setStyle("-fx-alignment: CENTER;");

        customerIDCol.setResizable(false);
        customerNameCol.setResizable(false);
        customerAddressCol.setResizable(false);
        customerPhoneCol.setResizable(false);
        isActiveStringCol.setResizable(false);

        customerTableView.setItems(customer.getAllCustomers());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setTableProperties();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
