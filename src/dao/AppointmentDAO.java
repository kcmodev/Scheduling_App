package dao;

import controller.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class AppointmentDAO {

    private static ZoneId userZone = ZoneId.of(Main.userZone.getID());
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static LocalDateTime localDateTime;
    private static ZonedDateTime zone;
    private static DateTimeFormatter dateTimeFormat;
    private static ObservableList<String> validHours = FXCollections.observableArrayList();
    private static ObservableList<String> validMinutes = FXCollections.observableArrayList();
    private static ObservableList<String> validMonths = FXCollections.observableArrayList();
    private static ObservableList<String> validDays = FXCollections.observableArrayList();
    private static ObservableList<String> validYears = FXCollections.observableArrayList();

    private static final Connection conn = ConnectionHandler.startConnection();
    private CustomerDAO customerData = new CustomerDAO();

    public void addAppointment(String name, String start, String end, String type) throws SQLException {
        CustomerDAO customerData = new CustomerDAO();
        StatementHandler statement = new StatementHandler();
        int custId = customerData.getCustomerIdByName(name);

        /**
         * formats start and end to be inserted into database as UTC by adding ghe current user's offset
         */
        System.out.println("unformatted start: " + start);
        String formattedStartDateTime = formatDateTimeForDB(start);
        System.out.println("unformatted end: " + end);
        String formattedEndDateTime = formatDateTimeForDB(end);

        String sqlStatement = "insert into appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) " +
                "values (?, 1, 'not needed', 'not needed', 'not needed', '', ?, 'not needed', " + formattedStartDateTime + ", " + formattedEndDateTime + ", DATE(NOW()), 'admin', 'admin')";

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

    public void modifyAppointment(int apptId, String start, String end, String type) throws SQLException {
        StatementHandler statement = new StatementHandler();

        System.out.println("unformatted start: " + start);
        String formattedStart = formatDateTimeForDB(start);
        System.out.println("unformatted end: " + end);
        String formattedEnd = formatDateTimeForDB(end);

        String sqlStatement = "update appointment set start = " + formattedStart + ", set end = " + formattedEnd + " , type = ? where appointmentId = ?";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, type);
        statement.getPreparedStatement().setInt(2, apptId);

        statement.getPreparedStatement().execute();
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

        String sqlStatement = "SELECT a.appointmentId, a.customerId, a.type, start FROM appointment a " +
                "JOIN customer c ON a.customerId = c.customerId " +
                "JOIN address addr ON c.addressId = addr.addressId " +
                "ORDER BY WEEK(start) ASC;";

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

        String sqlStatement = "SELECT a.appointmentId, a.customerId, a.type, start FROM appointment a " +
                "JOIN customer c ON a.customerId = c.customerId " +
                "JOIN address addr ON c.addressId = addr.addressId " +
                "ORDER BY MONTH(start), YEAR(start) ASC;";

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
                validMonths.add("0" + i); // add leading 0 for proper formatting
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
            return validDays.stream().filter(x -> Integer.parseInt(x) < 29).collect(Collectors.collectingAndThen(
                    Collectors.toList(), y -> FXCollections.observableArrayList(y)));
        }

        // leap year found
        if (month == 2 && currentYear.isLeap()) {
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
            /**
             * stream with a lambda to filter observable list to 30 days for
             * april, june, september, and november
             */
            return validDays.stream().filter(x -> Integer.parseInt(x) < 31).collect(Collectors.collectingAndThen(
                    Collectors.toList(), y -> FXCollections.observableArrayList(y)));
        }

        /**
         * else returns 31 days for rest of the months
         * jan, mar, may, jul, aug, oct, & dec
         */
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

            dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyyHH:mm:ss");
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

    public static final String getDate(String dateTime){
        String startDate = dateTime.substring(0, 10);
        return startDate;
    }

    public static final String getTime(String dateTime){
        String startTime = dateTime.substring(10, 18);
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
        System.out.println("local to zoned utc offset: " + localToZoned.getOffset());
        ZonedDateTime zonedToUTC = localToZoned.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("time converted to utc: " + zonedToUTC);

        dateTimeFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = zonedToUTC.format(dateTimeFormat);
        System.out.println("formatted: " + formattedDateTime);

        return formattedDateTime;
    }
}
