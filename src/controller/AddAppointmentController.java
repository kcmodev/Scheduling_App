package controller;

import ErrorHandling.InvalidInput;
import ErrorHandling.PopupHandlers;
import dao.AddressDAO;
import dao.AppointmentDAO;
import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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

    @FXML
    private ComboBox<String> names;
    @FXML
    private ComboBox<String> hours;
    @FXML
    private ComboBox<String> minutes;
    @FXML
    private ComboBox<String> years;
    @FXML
    private ComboBox<String> months;
    @FXML
    private ComboBox<String> days;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    @FXML
    private TextField type;


    public void setSaveClicked(ActionEvent event) throws SQLException {
        try {
            if (names.getSelectionModel().getSelectedIndex() == -1)
                throw new InvalidInput("You must select a customer");

            if (hours.getSelectionModel().getSelectedIndex() == -1 || minutes.getSelectionModel().getSelectedIndex() == -1)
                throw new InvalidInput("Must select both hour and minute of appointment time");

            if (years.getSelectionModel().getSelectedIndex() == -1 || months.getSelectionModel().getSelectedIndex() == -1
                    || days.getSelectionModel().getSelectedIndex() == -1)
                throw new InvalidInput("Month, day, and year must all be selected");

            if (type.getText().isEmpty())
                throw new InvalidInput("Appointment type can't be empty");

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
            String date = yr + "-" + mon + "-" + day + " ";
            String startTime = hrs + ":" + min + ":" + "00";
            String endTime = startTime;

            /**
             * concat a start and end timestamp for the sql query
             */
            String start = date + startTime;
            String end = date + endTime;

            appointmentData.addAppointment(customer, start, end, aptType);
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);

        } catch (InvalidInput i) {
            popups.errorAlert(2, i.getLocalizedMessage());
        }

    }

    public void setCancelClicked(ActionEvent event){
        if (popups.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }

    public void setRelated() throws SQLException {
        address.setText(customerData.getAddressByName(names.getValue()));
        phone.setText(addressData.getPhoneByName(names.getValue()));
    }

    public void onMonths(){
        if (months.getSelectionModel().isEmpty())
            months.setValue("01");
        if (years.getSelectionModel().isEmpty())
            years.setValue("2020");

        int month = Integer.parseInt(months.getValue());
        int year = Integer.parseInt(years.getValue());

        days.setItems(appointmentData.getValidDays(month, year));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalDate currentDay = LocalDate.now();

        try {
            names.setItems(CustomerDAO.getAllCustomerNames());
            hours.setItems(AppointmentDAO.getValidHours());
            minutes.setItems(AppointmentDAO.getValidMinutes());
            years.setItems(AppointmentDAO.getValidYears());
            months.setItems(AppointmentDAO.getValidMonths());
            days.setItems(AppointmentDAO.getValidDays(currentDay.getMonthValue(), currentDay.getYear()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
