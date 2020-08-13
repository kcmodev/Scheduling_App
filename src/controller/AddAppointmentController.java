package controller;

import ErrorHandling.PopupHandlers;
import dao.AddressDAO;
import dao.AppointmentDAO;
import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    private WindowManager window = new WindowManager();
    private PopupHandlers popups = new PopupHandlers();
    private CustomerDAO customerData = new CustomerDAO();
    private AppointmentDAO appointmentData = new AppointmentDAO();
    private AddressDAO addressData = new AddressDAO();

    @FXML private ChoiceBox<String> names;
    @FXML private ChoiceBox<String> hours;
    @FXML private ChoiceBox<String> minutes;
    @FXML private ChoiceBox<String> years;
    @FXML private ChoiceBox<String> months;
    @FXML private ChoiceBox<String> days;
    @FXML private TextField address;
    @FXML private TextField phone;
    @FXML private TextField type;

    public void setSaveClicked(ActionEvent event) throws SQLException {
        String customer = names.getValue();
        String hrs = hours.getValue();
        String min = minutes.getValue();
        String yr = years.getValue();
        String mon = months.getValue();
        String day = days.getValue();
        String aptType = type.getText();

        /**
         * convert date, start, and end time into strings and concat everything into the
         * correct format for a timestamp
         */
        String date = yr + mon + day;
        String startTime = hrs + min + "00";
        String endTime = startTime;

        /**
         * concat a start and end timestamp for the sql query
         */
        String start = date + startTime;
        String end = date + endTime;

        appointmentData.addAppointment(customer, start, end, aptType);
        window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);

    }

    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }

    public void setNames() throws SQLException {
        address.setText(customerData.getAddressByName(names.getValue()));
        phone.setText(addressData.getPhoneByName(names.getValue()));
    }

    public void onMonths(){ days.setItems(appointmentData.getValidDays(Integer.parseInt(months.getValue()), Integer.parseInt(years.getValue()))); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalDate currentDay = LocalDate.now();

        try {
            names.setItems(customerData.getAllCustomerNames());
            hours.setItems(appointmentData.getValidHours());
            minutes.setItems(appointmentData.getValidMinutes());
            years.setItems(appointmentData.getValidYears());
            months.setItems(appointmentData.getValidMonths());
            days.setItems(appointmentData.getValidDays(currentDay.getMonthValue(), currentDay.getYear()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
