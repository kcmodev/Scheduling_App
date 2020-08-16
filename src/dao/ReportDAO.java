package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private static Connection conn = ConnectionHandler.startConnection();

    public List getAppointmentsPerMonth() throws SQLException {
        StatementHandler statement = new StatementHandler();
        List<String> reportList = new ArrayList<>();
        reportList.add("Number of appointments per month:" + System.lineSeparator());
        String month;
        String count;

        String sqlStatement = "SELECT MONTH(a.start) month, COUNT(a.appointmentId) count FROM appointment a " +
                              "GROUP BY MONTH(a.start);";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()){
            month = Month.of(rs.getInt("month")).name();
            count = rs.getString("count");
            reportList.add("\t" + month + " has " + count + " appointments." + System.lineSeparator());
        }

        return reportList;
    }

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

    public List getAllAppointmentsForUser(String user) throws SQLException {
        StatementHandler statement = new StatementHandler();
        List<String> reportList = new ArrayList<>();

        String sqlStatement = "SELECT u.userName, a.type, " +
                "DATE_FORMAT(a.start, '%M %d at %h:%i') date, c.customerName, cty.city FROM user u " +
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
            String userName = rs.getString("userName");
            String customerName = rs.getString("customerName");
            String type = rs.getString("type");
            String when = rs.getString("date");
            String city = rs.getString("city");
            reportList.add("\t " + customerName + ": " + when + "\n" +
                    "\t for " + type + " in " + city + "\n" + System.lineSeparator());
        }

        return reportList;
    }
}
