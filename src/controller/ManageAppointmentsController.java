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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class ManageAppointmentsController implements Initializable {
    private final Connection conn = ConnectionHandler.startConnection();
    private final WindowManager window = new WindowManager();
    private final PopupHandlers popups = new PopupHandlers();
    private final AppointmentDAO appointmentData = new AppointmentDAO();
    private final CustomerDAO customerData = new CustomerDAO();

    @FXML private Label appointmentLabel;

    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> appointmentTypeCol;
    @FXML private TableColumn<Appointment, String> customerNameCol;
    @FXML private TableColumn<Appointment, String> customerAddressCol;
    @FXML private TableColumn<Appointment, String> customerPhoneCol;
    @FXML private TableColumn<Appointment, String> appointmentTimeCol;
    @FXML private TableColumn<Appointment, String> appointmentDateCol;

    @FXML private ToggleGroup filterSelection;
    @FXML private RadioButton all;
    @FXML private RadioButton week;
    @FXML private RadioButton month;


    /**
     * method dynamically sets label to the logged in user's username
     * @param label
     */
    public void setLabel(String label) { appointmentLabel.setText(label); }

    public void setAddClicked(ActionEvent event){
        window.windowController(event, "/gui/AddAppointment.fxml", window.ADD_APPOINTMENT_TITLE);
    }

    public void setUpdateClicked(ActionEvent event) throws IOException {

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
            popups.errorAlert(1, "You must choose an appointment to modify");
        }
    }

    /**
     * method handles delete button being clicked
     * removes selected customer from the database
     * returns an error if null
     */
    public void deleteClicked () {
        StatementHandler statement = new StatementHandler();

        try {
            String name = appointmentTableView.getSelectionModel().getSelectedItem().getName();
            String selectStatement = "SELECT customerName FROM customer\n" +
                    "WHERE customerName = ?;";

            statement.setPreparedStatement(conn, selectStatement);
            statement.getPreparedStatement().setString(1, name);
            ResultSet set = statement.getPreparedStatement().executeQuery();

            if (set.next()) {
                String deleteStatement = "DELETE FROM customer WHERE customerName = ?;";
                statement.setPreparedStatement(conn, deleteStatement);
                statement.getPreparedStatement().setString(1, name);

                for (Customer customer : customerData.getAllCustomers()){
                    if (customer.getName().equals(name)){
                        customerData.deleteCustomer(customer);
                        break;
                    }
                }
            }

            setFilterSelection();
            appointmentTableView.refresh();
            appointmentTableView.getSelectionModel().clearSelection();

        } catch (NullPointerException e) {
            popups.errorAlert(1, "You Must make a selection.");
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public void setLogOutClicked(ActionEvent event) {
        if (popups.confirmationAlert("log out")){
            window.windowController(event, "/gui/Login.fxml", window.LOGIN_SCREEN_TITLE);
        }
    }

    public void setFilterSelection() throws SQLException {
        System.out.println("setting filter selection");
        if (all.isSelected())
            setViewAll();
        if (week.isSelected())
            setViewWeek();
        if (month.isSelected())
            setViewMonth();
    }

    public void setViewAll() throws SQLException {
        System.out.println("filter set to \"view all\"");
        appointmentTableView.setItems(appointmentData.getAllAppointments());
    }

    public void setViewWeek(){ System.out.println("filter set to \"filter by week\""); }

    public void setViewMonth(){
        System.out.println("filter set to \"filter by month\"");
    }

    public void manageCustomersClicked(ActionEvent event){
        window.windowController(event, "/gui/ManageCustomers.fxml", window.MANAGE_CUSTOMERS_TITLE);
    }

    public void setTableProperties() {
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        appointmentDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        appointmentTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        appointmentTypeCol.setStyle("-fx-alignment: CENTER;");
        customerNameCol.setStyle("-fx-alignment: CENTER;");
        customerAddressCol.setStyle("-fx-alignment: CENTER;");
        customerPhoneCol.setStyle("-fx-alignment: CENTER;");
        appointmentDateCol.setStyle("-fx-alignment: CENTER;");
        appointmentTimeCol.setStyle("-fx-alignment: CENTER;");

        appointmentTypeCol.setResizable(false);
        customerNameCol.setResizable(false);
        customerAddressCol.setResizable(false);
        customerPhoneCol.setResizable(false);
        appointmentDateCol.setResizable(false);
        appointmentTimeCol.setResizable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLabel("Schedule for /ADMIN USER/");
        String zone = Main.userZone.toZoneId().toString();
        System.out.println("User time zone: " + zone);

        setTableProperties();

        try {
            setViewAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
