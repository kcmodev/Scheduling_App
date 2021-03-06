/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private static Connection conn = ConnectionHandler.startConnection();

    /**
     * makes database call to count the number of distinct appointment types and group them by month
     * ignores months with 0
     * @return
     * @throws SQLException
     */
    public List getAppointmentTypesPerMonth() throws SQLException {
        StatementHandler statement = new StatementHandler();
        List<String> reportList = new ArrayList<>();
        reportList.add("Number of distinct appointment types by month:\n" + System.lineSeparator());
        String month;
        String count;

        String sqlStatement = "SELECT MONTH(start) month, " +
                "COUNT(DISTINCT type) AS distinctType " +
                "FROM appointment " +
                "GROUP BY MONTH(start) ASC;";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            month = Month.of(rs.getInt("month")).name();
            count = rs.getString("distinctType");
            reportList.add("\t" + month + " has " + count + " distinct types of appointments." + System.lineSeparator());

            String findTypes = "SELECT MONTH(start) month, type, " +
                    "COUNT(DISTINCT type) AS distinctType " +
                    "FROM appointment " +
                    "WHERE MONTH(start) = ? " +
                    "GROUP BY MONTH(start), type ASC;";

            statement.setPreparedStatement(conn, findTypes);
            statement.getPreparedStatement().setInt(1, rs.getInt("month"));
            ResultSet nestedRS = statement.getPreparedStatement().executeQuery();

            reportList.add("\tDistinct appointment types for " + month + ": " + System.lineSeparator());
            while (nestedRS.next()){
                String type = nestedRS.getString("type");
                reportList.add("\t\t" + type + "\n");
            }
            reportList.add("\n");
        }
        return reportList;
    }

    /**
     * makes database call to find and count the number of appointments for every customer
     * @return
     * @throws SQLException
     */
    public List getAppointmentsPerCustomer() throws SQLException {
        StatementHandler statement = new StatementHandler();
        List<String> reportList = new ArrayList<>();
        reportList.add("Number of appointments per customer: " + System.lineSeparator());
        String customerName;
        String count;

        String sqlStatement = "SELECT c.customerName, COUNT(a.appointmentID) count FROM appointment a " +
                                "JOIN customer c on c.customerId = a.customerId " +
                                  "GROUP BY c.customerName;";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            customerName = rs.getString("customerName");
            count = rs.getString("count");
            reportList.add("\t" + customerName + " has " + count + " total appointments." + System.lineSeparator());
        }

        return reportList;
    }

    /**
     * makes database call to fine and call all appointments for the logged in user
     * @param user
     * @return
     * @throws SQLException
     */
    public List getAllAppointmentsForUser(String user) throws SQLException {
        StatementHandler statement = new StatementHandler();
        List<String> reportList = new ArrayList<>();

        String sqlStatement = "SELECT u.userName, a.type, " +
                "a.start, c.customerName, cty.city FROM user u " +
                "JOIN appointment a ON u.userId = a.userId " +
                "JOIN customer c ON a.customerId = c.customerId " +
                "JOIN address addr ON c.addressId = addr.addressId " +
                "JOIN city cty ON addr.cityId = cty.cityId " +
                "WHERE userName = ? " +
                "ORDER BY DATE(a.start);";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, user);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        reportList.add("Appointments for \"" + user + "\":\n");

        while (rs.next()){
            Timestamp start = rs.getTimestamp("start");
            LocalDateTime localTime = start.toLocalDateTime();
            ZonedDateTime localZone = localTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));

            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yy");
            String formattedDate = localZone.toOffsetDateTime().format(dateTimeFormat);

            dateTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = localZone.toOffsetDateTime().format(dateTimeFormat);

            String customerName = rs.getString("customerName");
            String type = rs.getString("type");
            String city = rs.getString("city");
            reportList.add("\tAppointment with: " + customerName + " on " + formattedDate + " at " + formattedTime + "\n" +
                    "\tfor \"" + type + "\" in " + city + "\n" + System.lineSeparator());
        }

        return reportList;
    }
}
