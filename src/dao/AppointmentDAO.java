package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentDAO {
    private static String sqlStatement;
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ResultSet rs;

    public void addAppointment() {

    }

    public void deleteAppointment() {

    }

    public void modifyAppointment() {

    }

    public static ObservableList<Appointment> setViewAllAppointments() throws SQLException {
        sqlStatement = "select a.customerId, a.type, time(a.start) startTime from appointment a\n" +
                          "join customer c on a.customerId = c.customerId;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            String type = rs.getString("type");
            System.out.println("type retrieved: " + type);
            int customerId = rs.getInt("customerId");
            System.out.println("customerId retrieved: " + customerId);
            String time = rs.getTime("startTime").toString();
            System.out.println("time retrieved: " + time);

            Appointment appointment = new Appointment(customerId, type, time);
            appointments.add(appointment);

        }

        return getAllAppointments();
    }

    public void setViewAllByWeek(){

    }

    public void setViewAllByMonth(){

    }

    public static ObservableList<Appointment> getAllAppointments() { return appointments; }
}
