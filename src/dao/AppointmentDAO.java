package dao;

import controller.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import models.Customer;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AppointmentDAO {
    private static ZoneId userZone = ZoneId.of(Main.userZone.getID());
    private static ZonedDateTime zonedTime;
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    private static ObservableList<String> validHours = FXCollections.observableArrayList();
    private static ObservableList<String> validMinutes = FXCollections.observableArrayList();
    private static ObservableList<String> validMonths = FXCollections.observableArrayList();
    private static ObservableList<String> validDays = FXCollections.observableArrayList();
    private static ObservableList<String> validYears = FXCollections.observableArrayList();

    private static final Calendar cal = Calendar.getInstance();
    private static final Connection conn = ConnectionHandler.startConnection();
    private CustomerDAO customerData = new CustomerDAO();

    public void addAppointment(String name, String start, String end, String type) throws SQLException {
        CustomerDAO customerData = new CustomerDAO();
        StatementHandler statement = new StatementHandler();

        int custId = customerData.getCustomerIdByName(name);

        String sqlStatement = "insert into appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy)\n" +
                "values (?, 1, 'not needed', 'not needed', 'not needed', '', ?, 'not needed', " + start + ", " + end + ", DATE(NOW()), 'admin', 'admin')";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, custId);
        statement.getPreparedStatement().setString(2, type);

        statement.getPreparedStatement().execute();

    }

    public void deleteAppointment(String name, String type) throws SQLException {
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "delete appointment from appointment " +
                "join customer on customer.customerId = appointment.customerId " +
                "where customerName = ? and type = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, name);
        statement.getPreparedStatement().setString(2, type);
        statement.getPreparedStatement().execute();
    }

    public void modifyAppointment(){

    }

    public void setViewAllByWeek(){ }

    public void setViewAllByMonth(){ }

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        buildAppointmentData();
        return appointments;
    }

    public static ObservableList<String> getValidHours() { return validHours; }

    /**
     * make available hours for appointments 09-16
     * omits 1700 due to business closing at 1700 hrs
     * last available input time 1645 hrs
     */
    public static void setValidHours() {
        System.out.println("hours built");
        validHours.add("09");
        for (int i = 10; i <= 16; i++)
            validHours.add(Integer.toString(i));
    }

    public static ObservableList<String> getValidMinutes() { return validMinutes; }

    /**
     * builds observable list of valid appointment time minites in increments of 15
     * 00, 15, 30, 45
     */
    public static void setValidMinutes() {
        System.out.println("minutes built");
        validMinutes.add("00");
        for (int i = 15; i <= 45; i += 15)
            validMinutes.add(Integer.toString(i));
    }

    public static ObservableList<String> getValidMonths() { return validMonths; }

    /**
     * builds observable list of valid months 1-12
     */
    public static void setValidMonths() {
        System.out.println("months built");
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                validMonths.add("0" + i);
            } else {
                validMonths.add(Integer.toString(i));
            }
        }
    }

    /**
     * determines the number of days to display to the user based on the month selected
     * to prevent invalid input and increase record keeping accuracy
     * @param month
     * @return
     */
    public static ObservableList<String> getValidDays(int month) {
        /**
         * checking if month is february and assigning either 28 or 29 days depending on if it is a leap year
         * first is not leap year
         */
        if (month == 2 && !(cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365)) {
            System.out.println("found february normal year, 289 days");
            /**
             * stream with a lambda to filter observable list to 28 days for february when selected
             */
            return validDays.stream().filter(x -> Integer.parseInt(x) < 29).collect(Collectors.collectingAndThen(
                    Collectors.toList(), y -> FXCollections.observableArrayList(y)));
        }

        // leap year found
        if (month == 2 && (cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365)) {
            System.out.println("found february leap year, 29 days");
            /**
             * stream with a lambda to filter observable list to 29 days for february when selected and
             * it happens to be a leap year
             */
            return validDays.stream().filter(x -> Integer.parseInt(x) < 30).collect(Collectors.collectingAndThen(
                    Collectors.toList(), y -> FXCollections.observableArrayList(y)));
        }

        /**
         * checks if month is april, june, september, or november
         * returns 30 days if true
         */
        if (month == 4 || month == 6 ||month == 9 || month == 11){
            System.out.println("found april, june, sept, or nov. 30 days.");
            /**
             * stream with a lambda to filter observable list to 30 days for
             * april, june, september, and november
             */
            return validDays.stream().filter(x -> Integer.parseInt(x) < 31).collect(Collectors.collectingAndThen(
                    Collectors.toList(), y -> FXCollections.observableArrayList(y)));
        }

        /**
         * else returns 31 days for rest of the months
         */
        System.out.println("found anything else, 31 days");
        return validDays;
    }

    /**
     * builds observable list of all valid days (1-31)
     */
    public static void setValidDays() {
        System.out.println("days built");
        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                validDays.add("0"+ i);
            }
            else validDays.add(Integer.toString(i));
        }
    }

    public static ObservableList<String> getValidYears() { return validYears; }

    /**
     * builds observable list of valid years between 2020-2025
     */
    public static void setValidYears() {
        System.out.println("years built");
        for (int i = 2020; i <= 2025; i++){
            validYears.add(Integer.toString(i));
        }
    }

    /**
     * clears and builds most recent list of appointments
     * @throws SQLException
     */
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

            Appointment appointment = new Appointment(customerId, type, startDate, startTime);
            appointments.add(appointment);
        }
    }
}
