package controller;

import dao.AppointmentDAO;
import dao.ConnectionHandler;
import dao.CustomerDAO;

import dao.StatementHandler;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Appointment;
import models.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ManageAppointmentsController implements Initializable {
    private String sqlStatement;
    WindowManager window = new WindowManager();

    @FXML private Label appointmentLabel;

    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> appointmentTypeCol;
    @FXML private TableColumn<Appointment, String> customerNameCol;
    @FXML private TableColumn<Appointment, String> customerAddressCol;
    @FXML private TableColumn<Appointment, String> customerPhoneCol;
    @FXML private TableColumn<Appointment, Time> appointmentDateTimeCol;

    @FXML private ToggleGroup filterSelection;
    @FXML private RadioButton all;
    @FXML private RadioButton week;
    @FXML private RadioButton month;


    /**
     * method dynamically sets label to the logged in user's username
     * @param label
     */
    public void setLabel(String label) {
        System.out.println("makes it to set label with: \"" + label + "\"");
        appointmentLabel.setText(label);
    }

    public void setAddClicked(ActionEvent event){
        System.out.println("add button clicked");
        window.windowController(event, "/gui/AddAppointment.fxml", WindowManager.ADD_APPOINTMENT_TITLE);
    }

    public void setUpdateClicked(ActionEvent event) throws IOException {
        System.out.println("update button clicked");

        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/UpdateAppointment.fxml"));
            Parent parent = loader.load();
            Scene modPartScene = new Scene(parent);

            UpdateAppointmentController controller = loader.getController();
            controller.setTextFields(appointmentTableView.getSelectionModel().getSelectedItem());

            Stage newWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
            newWindow.setScene(modPartScene);
            newWindow.setResizable(false);
            newWindow.setTitle(WindowManager.UPDATE_APPOINTMENT_TITLE);
            newWindow.show();
        } else {
            PopupHandlers.errorAlert(1, "You must choose an appointment to modify");
        }
    }

    /**
     * method handles delete button being clicked
     * removes selected customer from the database
     * returns an error if null
     */
    public void deleteClicked () {
        System.out.println("delete button clicked");

//        try {
//            String name = appointmentTableView.getSelectionModel().getSelectedItem().getName();
//            sqlStatement = "SELECT customerName FROM customer\n" +
//                    "WHERE customerName = ?;";
//            System.out.println("attempting to delete: \"" + name + "\"");
//            System.out.println("sql statement being passed in: " + sqlStatement);
//
//            StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
//            StatementHandler.getPreparedStatement().setString(1, name);
//            ResultSet set = StatementHandler.getPreparedStatement().executeQuery();
//
//            if (set.next()) {
//                sqlStatement = "DELETE FROM customer WHERE customerName = ?;";
//                System.out.println("new sql statement: \"" + sqlStatement + "\"");
//                StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
//                StatementHandler.getPreparedStatement().setString(1, name);
////                StatementHandler.getPreparedStatement().execute();
//
//                for (Customer customer : CustomerDAO.getAllCustomers()){
//                    if (customer.getName().equals(name)){
//                        System.out.println("deleting: " + customer.getName());
//                        CustomerDAO.deleteCustomer(customer);
//                        break;
//                    }
//                }
//            }
//
//            setFilterSelection();
//            appointmentTableView.refresh();
//            appointmentTableView.getSelectionModel().clearSelection();
//
//        } catch (NullPointerException | SQLException e) {
//            PopupHandlers.errorAlert(1, "You Must make a selection.");
//        }
    }

    public void setLogOutClicked(ActionEvent event) {
        if (PopupHandlers.confirmationAlert("log out")){
            window.windowController(event, "/gui/Login.fxml", WindowManager.LOGIN_SCREEN_TITLE);
        }
    }

    public void setFilterSelection() throws SQLException {
        System.out.println("filtering selection");
        if (all.isSelected())
            setViewAll();
        if (week.isSelected())
            setViewWeek();
        if (month.isSelected())
            setViewMonth();
    }

    public void setViewAll() throws SQLException {
        System.out.println("filter set to \"view all\"");
        appointmentTableView.setItems(AppointmentDAO.getAllAppointments());
    }

    public void setViewWeek(){
        System.out.println("filter set to \"filter by week\"");

    }

    public void setViewMonth(){
        System.out.println("filter set to \"filter by month\"");
    }

    public void manageCustomersClicked(ActionEvent event){
        System.out.println("manage customers button clicked");
        window.windowController(event, "/gui/ManageCustomers.fxml", WindowManager.MANAGE_CUSTOMERS_TITLE);
    }

    public void setTableProperties() {
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        appointmentDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        appointmentTypeCol.setStyle("-fx-alignment: CENTER;");
        customerNameCol.setStyle("-fx-alignment: CENTER;");
        customerAddressCol.setStyle("-fx-alignment: CENTER;");
        customerPhoneCol.setStyle("-fx-alignment: CENTER;");
        appointmentDateTimeCol.setStyle("-fx-alignment: CENTER;");

        appointmentTypeCol.setResizable(false);
        customerNameCol.setResizable(false);
        customerAddressCol.setResizable(false);
        customerPhoneCol.setResizable(false);
        appointmentDateTimeCol.setResizable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLabel("Schedule for /ADMIN USER/");
        String zone = Main.userZone.toZoneId().toString();
        System.out.println("User time zone: " + zone);
        setTableProperties();
        try {
            setViewAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
