package dao;

import controller.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import models.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class AppointmentDAO {
    private static ZoneId userZone = ZoneId.of(Main.userZone.getID());
    private static ZonedDateTime zonedTime;
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    private ObservableList<String> validHours = FXCollections.observableArrayList();
    private ObservableList<String> validMinutes = FXCollections.observableArrayList();
    private ObservableList<String> validMonths = FXCollections.observableArrayList();
    private ObservableList<String> validDays = FXCollections.observableArrayList();
    private ObservableList<String> validYears = FXCollections.observableArrayList();

    private static final Connection conn = ConnectionHandler.startConnection();


    public static void buildAppointmentData() throws SQLException {
        appointments.clear();
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "SELECT customerId, type, DATE(start) apptDate, TIME(start) apptTime FROM appointment";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            int customerId = rs.getInt("customerId");
            String type = rs.getString("type");
            String startDate = rs.getString("apptDate");
            String startTime = rs.getString("apptTime");

//            System.out.println("zone id found: " + userZone);

//            ZonedDateTime timestamp = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC"));
//            String time = timestamp.toString().replace("Z[UTC]", "");
//            System.out.println("char to replace: " + time.charAt(10));
//            time = time.replace("T", "  |  ");
//            time.replaceAll("^[a-z]$", "  |  ");
//            System.out.println("date/time retrieved: " + time);

            Appointment appointment = new Appointment(customerId, type, startDate, startTime);
            appointments.add(appointment);
        }
    }

//    public String getAppointmentDate(String dateTime){
//        String sqlStatement = "";
//    }
//
//    public String getAppointmentTime(String dateTime){
//
//    }

    public void addAppointment(){ }

    public void deleteAppointment(){ }

    public void modifyAppointment(){ }

    public void setViewAllByWeek(){ }

    public void setViewAllByMonth(){ }

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        buildAppointmentData();
        return appointments;
    }

    public ObservableList<String> getValidHours() { return validHours; }

    /**
     * make available hours for appointments 09-16
     * omits 1700 due to business closing at 1700 hrs
     * @param validHours
     */
    public void setValidHours(ObservableList<String> validHours) {
        for (int i = 9; i <= 16; i++)
            validHours.add(Integer.toString(i));
        this.validHours = validHours;
    }

    public ObservableList<String> getValidMinutes() { return validMinutes; }

    public void setValidMinutes(ObservableList<String> validMinutes) {
        validMinutes.add("00");
        for (int i = 15; i <= 45; i++)
            validMinutes.add(Integer.toString(i));
        this.validMinutes = validMinutes;
    }

    public ObservableList<String> getValidMonths() { return validMonths; }

    public void setValidMonths(ObservableList<String> validMonths) {
        for (int i = 1; i <= 12; i++)
            validMonths.add(Integer.toString(i));
        this.validMonths = validMonths;
    }

    public ObservableList<String> getValidDays(int month) { return validDays; }

    public void setValidDays(ObservableList<String> validDays) {
        for (int i = 1; i <= 31; i++)
            validDays.add(Integer.toString(i));
        this.validDays = validDays;
    }

    public ObservableList<String> getValidYears() { return validYears; }

    public void setValidYears(ObservableList<String> validYears) {
        this.validYears = validYears;
    }
}
