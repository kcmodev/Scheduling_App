package dao;

import controller.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class AppointmentDAO {
    private static String sqlStatement;
    private static ZoneId userZone = ZoneId.of(Main.userZone.getID());
    private static ZonedDateTime zonedTime;
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ResultSet rs;

    public static void buildAppointmentData() throws SQLException {
        appointments.clear();
        sqlStatement = "select a.customerId, a.type, a.start from appointment a\n" +
                "join customer c on a.customerId = c.customerId;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            String type = rs.getString("type");
            System.out.println("type retrieved: " + type);
            int customerId = rs.getInt("customerId");
            System.out.println("customerId retrieved: " + customerId);
            System.out.println("zone id found: " + userZone);

//            ZonedDateTime timestamp = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC"));
//            String time = timestamp.toString().replace("Z[UTC]", "");
//            System.out.println("char to replace: " + time.charAt(10));
//            time = time.replace("T", "  |  ");
//            time.replaceAll("^[a-z]$", "  |  ");
            String time = rs.getString("start").replaceAll("[ ]", "  |  ");
//            System.out.println("date/time retrieved: " + time);

            Appointment appointment = new Appointment(customerId, type, time);
            appointments.add(appointment);
        }
    }

    public void addAppointment() {

    }

    public void deleteAppointment() {

    }

    public void modifyAppointment() {

    }

    public static void setViewAllByWeek(){
        sqlStatement = "";
    }

    public void setViewAllByMonth(){ }

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        buildAppointmentData();
        return appointments;
    }
}
