/**
 * Author: Steven Christensen
 * Email: schr206@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHandler {

    /**
     * set each part of URL for database
     * and concat vars for full url
     */
    private static final String PROTOCOL = "jdbc";
    private static final String VENDOR = ":mysql:";
    private static final String ADDRESS = "//wgudb.ucertify.com/U07Rzg";
    private static final String URL = PROTOCOL + VENDOR + ADDRESS;

    /**
     * username and pass for database access
     */
    private static final String USER_NAME = "U07Rzg";
    private static final String PASSWORD = "53689110359";

    /**
     * set location or driver jar file
     */
    private static final String SQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    /**
     * handles opening new connection
     * @return connection
     */
    public static Connection startConnection(){
        try {
            Class.forName(SQL_DRIVER);
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    /**
     * handles terminating connection
     */
    public static void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
