/**
 * Author: Steven Christensen
 * Email: schr206@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package controller;

import ErrorHandling.PopupHandlers;
import dao.AppointmentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Appointment;

import java.sql.SQLException;


public class UpdateAppointmentController {
    private Appointment tempAppt;
    private static final WindowManager window = new WindowManager();
    private static final PopupHandlers popups = new PopupHandlers();
    private static final AppointmentDAO appointmentData = new AppointmentDAO();

    @FXML
    private TextField names;
    @FXML
    private TextField addresses;
    @FXML
    private TextField phoneNumbers;
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
    private TextField types;

    /**
     * handles save button clicks
     * @param event
     * @throws SQLException
     */
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

    /**
     * handles cancel button clicks
     * @param event
     */
    public void setCancelClicked(ActionEvent event) {
        if (popups.confirmationAlert("quit and discard unsaved changes")) {
            window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        }
    }

    /**
     * sets the correct amount of days in the days combo box based on the month and year
     */
    public void onMonths() {
        if (months.getValue() != null) {
            days.setItems(appointmentData.getValidDays(Integer.parseInt(months.getValue()), Integer.parseInt(years.getValue())));
        }
    }

    /**
     * stores a temporary appointment object for comparison use
     * sets text fields with the data from that appointment
     * @param appointment
     */
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
        String mnth = date.substring(0, 2);
        String day = date.substring(3, 5);
        String yr = date.substring(6, 10);

        /**
         * populates combo boxes with the appropriate starting data
         */
        hours.setItems(AppointmentDAO.getValidHours());
        minutes.setItems(AppointmentDAO.getValidMinutes());

        hours.setValue(hr);
        minutes.setValue(min);

        years.setItems(AppointmentDAO.getValidYears());
        months.setItems(AppointmentDAO.getValidMonths());
        days.setItems(AppointmentDAO.getValidDays(Integer.parseInt(mnth), Integer.parseInt(yr)));

        years.setValue(yr);
        months.setValue(mnth);
        days.setValue(day);


    }
}
