/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package dao;

import ErrorHandling.AppointmentTimeWarning;
import ErrorHandling.PopupHandlers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.Appointment;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.stream.Collectors;

public class AppointmentDAO {

    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static LocalDateTime localDateTime;
    private static ZonedDateTime zone;
    private static DateTimeFormatter dateTimeFormat;
    private static final Calendar cal = Calendar.getInstance();
    private static final PopupHandlers popups = new PopupHandlers();

    private static final ObservableList<String> VALID_HOURS = FXCollections.observableArrayList();
    private static final ObservableList<String> VALID_MINUTES = FXCollections.observableArrayList();
    private static final ObservableList<String> VALID_MONTHS = FXCollections.observableArrayList();
    private static final ObservableList<String> VALID_DAYS = FXCollections.observableArrayList();
    private static final ObservableList<String> VALID_YEARS = FXCollections.observableArrayList();

    private static final Connection conn = ConnectionHandler.startConnection();

    /**
     * adds appointment record to the database
     * @param name
     * @param start
     * @param end
     * @param type
     * @throws SQLException
     */
    public void addAppointment(String name, String start, String end, String type) throws SQLException {
        CustomerDAO customerData = new CustomerDAO();
        StatementHandler statement = new StatementHandler();
        int custId = customerData.getCustomerIdByName(name);

        /**
         * formats start and end to be inserted into database as UTC by adding ghe current user's offset
         */
        String formattedStartDateTime = formatDateTimeForDB(start);
        String formattedEndDateTime = formatDateTimeForDB(end);

        if (!isAppointmentTaken(formattedStartDateTime)) {
            String sqlStatement = "insert into appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) " +
                    "values (?, 1, 'not needed', 'not needed', 'not needed', '', ?, 'not needed', " + formattedStartDateTime + ", " + formattedEndDateTime + ", DATE(NOW()), 'admin', 'admin')";

            statement.setPreparedStatement(conn, sqlStatement);
            statement.getPreparedStatement().setInt(1, custId);
            statement.getPreparedStatement().setString(2, type);

            statement.getPreparedStatement().execute();
        } else {
            popups.errorAlert(2, "You already have an appointment at that time");
        }

    }

    /**
     * removes appointment record from the database
     * @param name
     * @param type
     * @throws SQLException
     */
    public void deleteAppointment(String name, String type) throws SQLException {
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "DELETE appointment FROM appointment " +
                "JOIN customer ON customer.customerId = appointment.customerId " +
                "WHERE customerName = ? AND type = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, name);
        statement.getPreparedStatement().setString(2, type);
        statement.getPreparedStatement().execute();
    }

    /**
     * updates existing appointment record
     * @param apptId
     * @param start
     * @param end
     * @param type
     * @throws SQLException
     */
    public void modifyAppointment(int apptId, String start, String end, String type) throws SQLException {
        StatementHandler statement = new StatementHandler();

        String formattedStart = formatDateTimeForDB(start);
        String formattedEnd = formatDateTimeForDB(end);

        String checkForConflict = "SELECT start FROM appointment WHERE NOT start = " + formattedStart + ";";
        statement.setPreparedStatement(conn, checkForConflict);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        try {
            if (!rs.next()) {
                String updateExisting = "UPDATE appointment SET start = " + formattedStart + ", end = " + formattedEnd + " , type = ? where appointmentId = ?";

                statement.setPreparedStatement(conn, updateExisting);
                statement.getPreparedStatement().setString(1, type);
                statement.getPreparedStatement().setInt(2, apptId);

                statement.getPreparedStatement().execute();
            } else {
                throw new AppointmentTimeWarning("An appointment at this time already exists");
            }
        } catch (AppointmentTimeWarning a){
            popups.errorAlert(2, a.getLocalizedMessage());
        }
    }

    /**
     * uses sql to sort the appointments by week, stores them in an observable array and
     * returns said array
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointment> setViewAllByWeek() throws SQLException {
        ObservableList<Appointment> filtered = FXCollections.observableArrayList();
        StatementHandler statement = new StatementHandler();
        dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyyHH:mm");

        String sqlStatement = "SELECT a.appointmentId, a.customerId, a.type, start FROM appointment a " +
                "JOIN customer c ON a.customerId = c.customerId " +
                "JOIN address addr ON c.addressId = addr.addressId " +
                "WHERE WEEK(start) = " + (cal.get(Calendar.WEEK_OF_YEAR) - 1) + ";";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            int apptId = rs.getInt("appointmentId");
            int custId = rs.getInt("customerId");
            String type = rs.getString("type");

            Timestamp start = rs.getTimestamp("start");
            localDateTime = start.toLocalDateTime();
            zone = localDateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            String formattedDateTime = zone.toOffsetDateTime().format(dateTimeFormat);

            String startDate = getDate(formattedDateTime);
            String startTime = getTime(formattedDateTime);

            Appointment appt = new Appointment(apptId, custId, type, startDate, startTime);
            filtered.add(appt);
        }

        return filtered;
    }

    /**
     * uses sql to sort the appointments by month, stores them in an observable array and
     * returns said array
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointment> setViewAllByMonth() throws SQLException {
        ObservableList<Appointment> filtered = FXCollections.observableArrayList();
        StatementHandler statement = new StatementHandler();
        dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyyHH:mm");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");

        String sqlStatement = "SELECT a.appointmentId, a.customerId, a.type, start FROM appointment a " +
                "JOIN customer c ON a.customerId = c.customerId " +
                "JOIN address addr ON c.addressId = addr.addressId " +
                "WHERE MONTH(start) = " + simpleDateFormat.format(cal.getTime()) + ";";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            int apptId = rs.getInt("appointmentId");
            int custId = rs.getInt("customerId");
            String type = rs.getString("type");

            Timestamp start = rs.getTimestamp("start");
            localDateTime = start.toLocalDateTime();
            zone = localDateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            String formattedDateTime = zone.toOffsetDateTime().format(dateTimeFormat);

            String startDate = getDate(formattedDateTime);
            String startTime = getTime(formattedDateTime);


            Appointment appt = new Appointment(apptId, custId, type, startDate, startTime);
            filtered.add(appt);
        }

        return filtered;
    }

    /**
     * method returns current appointment list
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        buildAppointmentDataForTables();
        return appointments;
    }

    /**
     * checks if there is a start time and date match in the database
     * @param start
     * @return
     * @throws SQLException
     */
    public boolean isAppointmentTaken(String start) throws SQLException{
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "SELECT a.appointmentId, a.customerId, start FROM appointment a " +
                                "JOIN customer c ON a.customerId = c.customerId " +
                                "JOIN address addr ON c.addressId = addr.addressId " +
                                "WHERE start = " + start + ";";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        return (rs.next()) ? true : false;
    }

    /**
     * checks for an appointment within 15 minutes of user's system time
     * @throws SQLException
     */
    public void isAppointmentNearNow() throws SQLException {
        StatementHandler statement = new StatementHandler();
        LocalDateTime systemTime = LocalDateTime.now();
        dateTimeFormat = DateTimeFormatter.ofPattern("HH:mm");

        String sqlStatement = "SELECT c.customerName, a.type, a.start, TIME(a.start) startTime FROM appointment a " +
                                 "JOIN customer c ON a.customerId = c.customerId;";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            String name = rs.getString("customerName");
            String type = rs.getString("type");

            Timestamp start = rs.getTimestamp("start");
            localDateTime = start.toLocalDateTime();
            zone = localDateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            String formattedDateTime = zone.toOffsetDateTime().format(dateTimeFormat);

            try {
                if (localDateTime.isAfter(systemTime) && localDateTime.isBefore(ChronoLocalDateTime.from(systemTime.plusMinutes(15))))
                    throw new AppointmentTimeWarning("You have an appointment coming up at " + formattedDateTime + " with " +
                            name + " for \"" + type + "\".");
                System.out.println();
            } catch (AppointmentTimeWarning a){
                Alert appointmentSoon = new Alert(Alert.AlertType.INFORMATION);
                appointmentSoon.setHeaderText("Notice");
                appointmentSoon.setTitle("Appointment soon");
                appointmentSoon.setContentText(a.getLocalizedMessage());
                appointmentSoon.showAndWait();
            }
        }
    }

    /**
     * clears and builds most recent list of appointments
     * @throws SQLException
     */
    public static void buildAppointmentDataForTables() throws SQLException {
        appointments.clear();
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "SELECT appointmentId, customerId, type, start FROM appointment;";
        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            int appointmentId = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            String type = rs.getString("type");

            dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyyHH:mm");
            Timestamp start = rs.getTimestamp("start");
            localDateTime = start.toLocalDateTime();
            zone = localDateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            String formattedDateTime = zone.toOffsetDateTime().format(dateTimeFormat);

            String startDate = getDate(formattedDateTime);
            String startTime = getTime(formattedDateTime);

            Appointment appointment = new Appointment(appointmentId, customerId, type, startDate, startTime);
            appointments.add(appointment);
        }
    }

    /**
     * returns date from string
     * @param dateTime
     * @return
     */
    public static final String getDate(String dateTime){
        String startDate = dateTime.substring(0, 10);
        return startDate;
    }

    /**
     * returns time from string
     * @param dateTime
     * @return
     */
    public static final String getTime(String dateTime){
        String startTime = dateTime.substring(10, 15);
        return startTime;
    }

    /**
     * formats incoming dateTimes into UTC for input into database
     * @param dateTime
     * @return
     */
    public static final String formatDateTimeForDB(String dateTime){
        Timestamp startDateTime = Timestamp.valueOf(dateTime);
        localDateTime = startDateTime.toLocalDateTime();
        ZonedDateTime localToZoned = localDateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime zonedToUTC = localToZoned.withZoneSameInstant(ZoneId.of("UTC"));

        dateTimeFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = zonedToUTC.format(dateTimeFormat);

        return formattedDateTime;
    }

    /**
     * returns list of valid hours for appointment times
     * @return
     */
    public static ObservableList<String> getValidHours() { return VALID_HOURS; }

    /**
     * make available hours for appointments 09-16
     * omits 1700 due to business closing at 1700 hrs
     * last available input time 1645 hrs
     */
    public static void setValidHours() {
        VALID_HOURS.add("09");
        for (int i = 10; i <= 16; i++)
            VALID_HOURS.add(Integer.toString(i));
    }

    /**
     * returns list of valid minutes for appointment times
     * @return
     */
    public static ObservableList<String> getValidMinutes() { return VALID_MINUTES; }

    /**
     * builds observable list of valid appointment time minites in increments of 15
     * 00, 15, 30, 45
     */
    public static void setValidMinutes() {
        VALID_MINUTES.add("00"); // adds "00" to beginning of list to avoid formatting issues
        for (int i = 15; i <= 45; i += 15)
            VALID_MINUTES.add(Integer.toString(i));
    }

    public static ObservableList<String> getValidMonths() { return VALID_MONTHS; }

    /**
     * builds observable list of valid months 1-12
     */
    public static void setValidMonths() {
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                VALID_MONTHS.add("0" + i); // add leading 0 for proper formatting
            } else {
                VALID_MONTHS.add(Integer.toString(i));
            }
        }
    }

    /**
     * determines the number of days to display to the user based on the month selected
     * to prevent invalid input and increase record keeping accuracy
     * @param month
     * @return
     */
    public static ObservableList<String> getValidDays(int month, int year) {
        Year currentYear = Year.of(year);

        /**
         * checking if month is february and assigning either 28 or 29 days depending on if it is a leap year
         * first is not leap year
         */
        if (month == 2 && !currentYear.isLeap()) {
            /**
             * stream with a lambda to filter observable list to 28 days for february when selected
             */
            return VALID_DAYS.stream().filter(x -> Integer.parseInt(x) < 29).collect(Collectors.collectingAndThen(
                    Collectors.toList(), y -> FXCollections.observableArrayList(y)));
        }

        // leap year found
        if (month == 2 && currentYear.isLeap()) {
            /**
             * stream with a lambda to filter observable list to 29 days for february when selected and
             * it happens to be a leap year
             */
            return VALID_DAYS.stream().filter(x -> Integer.parseInt(x) < 30).collect(Collectors.collectingAndThen(
                    Collectors.toList(), y -> FXCollections.observableArrayList(y)));
        }

        /**
         * checks if month is april, june, september, or november
         * returns 30 days if true
         */
        if (month == 4 || month == 6 ||month == 9 || month == 11){
            /**
             * stream with a lambda to filter observable list to 30 days for
             * april, june, september, and november
             */
            return VALID_DAYS.stream().filter(x -> Integer.parseInt(x) < 31).collect(Collectors.collectingAndThen(
                    Collectors.toList(), y -> FXCollections.observableArrayList(y)));
        }

        /**
         * else returns 31 days for rest of the months
         * jan, mar, may, jul, aug, oct, & dec
         */
        return VALID_DAYS;
    }

    /**
     * builds observable list of all valid days (1-31)
     */
    public static void setValidDays() {
        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                VALID_DAYS.add("0"+ i);
            }
            else VALID_DAYS.add(Integer.toString(i));
        }
    }

    public static ObservableList<String> getValidYears() { return VALID_YEARS; }

    /**
     * builds observable list of valid years between 2020-2025
     */
    public static void setValidYears() {
        for (int i = 2020; i <= 2025; i++){
            VALID_YEARS.add(Integer.toString(i));
        }
    }
}
