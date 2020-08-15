package controller;

import ErrorHandling.PopupHandlers;
import dao.AppointmentDAO;

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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageAppointmentsController implements Initializable {
    private final WindowManager window = new WindowManager();
    private final PopupHandlers popups = new PopupHandlers();
    private final AppointmentDAO appointmentData = new AppointmentDAO();

    @FXML
    private Label appointmentLabel;
    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentTypeCol;
    @FXML
    private TableColumn<Appointment, String> customerNameCol;
    @FXML
    private TableColumn<Appointment, String> customerAddressCol;
    @FXML
    private TableColumn<Appointment, String> customerPhoneCol;
    @FXML
    private TableColumn<Appointment, String> appointmentTimeCol;
    @FXML
    private TableColumn<Appointment, String> appointmentDateCol;
    @FXML
    private RadioButton all;
    @FXML
    private RadioButton week;
    @FXML
    private RadioButton month;


    /**
     * method dynamically sets label to the logged in user's username
     * @param label
     */
    public void setLabel(String label) { appointmentLabel.setText(label); }

    /**
     * handles add button being clicked
     * @param event
     */
    public void setAddClicked(ActionEvent event){
        window.windowController(event, "/gui/AddAppointment.fxml", window.ADD_APPOINTMENT_TITLE);
    }

    /**
     * handles update button clicked
     * sends selected object to the next screen
     * @param event
     * @throws IOException
     */
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
    public void deleteClicked () throws SQLException {

        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            if (popups.confirmationAlert("delete this appointment")) {
                String name = appointmentTableView.getSelectionModel().getSelectedItem().getName();
                String type = appointmentTableView.getSelectionModel().getSelectedItem().getType();

                appointmentData.deleteAppointment(name, type);

                setFilterSelection();
                appointmentTableView.refresh();
                appointmentTableView.getSelectionModel().clearSelection();
            }
        } else {
            popups.errorAlert(1, "You must select an appointment to delete");
        }

    }

    /**
     * handles the log out button being clicked
     * @param event
     */
    public void setLogOutClicked(ActionEvent event) {
        if (popups.confirmationAlert("log out")){
            window.windowController(event, "/gui/Login.fxml", window.LOGIN_SCREEN_TITLE);
        }
    }

    /**
     * sets the "filter by" radio button when coming back from another screen and handles action
     * on a selection being made
     * default is "all"
     * @throws SQLException
     */
    public void setFilterSelection() throws SQLException {
        System.out.println("setting filter selection");
        if (all.isSelected())
            setViewAll();
        if (week.isSelected())
            setViewWeek();
        if (month.isSelected())
            setViewMonth();
    }

    /**
     * sets view all and activates on "all" radio button click
     * @throws SQLException
     */
    public void setViewAll() throws SQLException {
        appointmentTableView.setItems(appointmentData.getAllAppointments());
    }

    /**
     * sets sort by week and activates on "week" radio button click
     * @throws SQLException
     */
    public void setViewWeek() throws SQLException {
        appointmentTableView.setItems(appointmentData.setViewAllByWeek());
    }

    /**
     * sets sort by month and activates on "month" radio button click
     * @throws SQLException
     */
    public void setViewMonth() throws SQLException {
        appointmentTableView.setItems(appointmentData.setViewAllByMonth());
    }

    public void manageCustomersClicked(ActionEvent event){
        window.windowController(event, "/gui/ManageCustomers.fxml", window.MANAGE_CUSTOMERS_TITLE);
    }

    public void reportsButton(ActionEvent event){

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
        appointmentDateCol.setSortable(false); // disable sorting by column, directing user to use radio buttons instead
        appointmentTimeCol.setResizable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLabel("Schedule of appointments for " + LoginController.currentUser);
        setTableProperties();

        try {
            setViewAll();
            appointmentData.isAppointmentNearNow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
