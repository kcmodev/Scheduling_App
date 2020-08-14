package controller;

import ErrorHandling.PopupHandlers;
import dao.AppointmentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Appointment;

import java.sql.SQLException;


public class UpdateAppointmentController {
    Appointment tempAppt;
    private WindowManager window = new WindowManager();
    private PopupHandlers popups = new PopupHandlers();
    private AppointmentDAO appointmentData = new AppointmentDAO();

    private static String yr;
    private static String mnth;

    @FXML
    private TextField names;
    @FXML
    private TextField addresses;
    @FXML
    private TextField phoneNumbers;
    @FXML
    private ChoiceBox<String> hours;
    @FXML
    private ChoiceBox<String> minutes;
    @FXML
    private ChoiceBox<String> years;
    @FXML
    private ChoiceBox<String> months;
    @FXML
    private ChoiceBox<String> days;
    @FXML
    private TextField types;

    public void setSaveClicked(ActionEvent event) throws SQLException {

        String newHr = hours.getValue();
        String newMin = minutes.getValue();
        String newYr = years.getValue();
        String newMnth = months.getValue();
        String newDay = days.getValue();
        String newType = types.getText();
        String c = ":";
        String d = "-";

        String startDateTime = newYr + d + newMnth + d + newDay + " " + newHr + c + newMin + c + "00"; // trailing zeroes for seconds
        String endDateTime = startDateTime;

        appointmentData.modifyAppointment(tempAppt.getAppointmentId(), startDateTime, endDateTime, newType);
        window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
    }

    public void setCancelClicked(ActionEvent event) {
        if (popups.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }

    public void onMonths() {
        if (months.getValue() != null) {
            days.setItems(appointmentData.getValidDays(Integer.parseInt(months.getValue()), Integer.parseInt(years.getValue())));
        }
    }

    public void onYears() { onMonths(); }

    public void setTextFields(Appointment appointment) {
        tempAppt = appointment;
        String name = appointment.getName();
        String address = appointment.getAddress();
        String type = appointment.getType();
        String phone = appointment.getPhone();

        names.setText(name);
        addresses.setText(address);
        types.setText(type);
        phoneNumbers.setText(phone);

        /**
         * take in date and time separately from incoming appointment
         */
        String time = appointment.getStartTime();
        String date = appointment.getStartDate();
        String hr = time.substring(0, 2);
        String min = time.substring(3, 5);
        mnth = date.substring(0, 2);
        String day = date.substring(3, 5);
        yr = date.substring(6, 10);

        hours.setItems(appointmentData.getValidHours());
        minutes.setItems(appointmentData.getValidMinutes());

        hours.setValue(hr);
        minutes.setValue(min);

        years.setItems(appointmentData.getValidYears());
        months.setItems(appointmentData.getValidMonths());
        days.setItems(appointmentData.getValidDays(Integer.parseInt(mnth), Integer.parseInt(yr)));

        years.setValue(yr);
        months.setValue(mnth);
        days.setValue(day);


    }
}
