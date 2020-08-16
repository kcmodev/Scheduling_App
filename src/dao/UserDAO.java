/**
 * Author: Steven Christensen
 * Email: schr206@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection conn = ConnectionHandler.startConnection();

    /**
     * checks if user exists
     * @param name
     * @return
     * @throws SQLException
     */
    public boolean isUser(String name) throws SQLException {
       StatementHandler statement = new StatementHandler();

       String sqlStatement = "SELECT userName FROM user WHERE userName = ?;";

       statement.setPreparedStatement(conn, sqlStatement);
       statement.getPreparedStatement().setString(1, name);
       ResultSet rs = statement.getPreparedStatement().executeQuery();

       return (rs.next()) ? true : false;
    }

    /**
     * checks if user and password match
     * @param name
     * @param pass
     * @return
     * @throws SQLException
     */
    public boolean passwordMatch(String name, String pass) throws SQLException {
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "SELECT userName, password FROM user WHERE userName = ? AND password = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, name);
        statement.getPreparedStatement().setString(2, pass);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        return (rs.next()) ? true : false;
    }

    /**
     * returns list of all user names
     * @return
     * @throws SQLException
     */
    public ObservableList<String> getAllUsers() throws SQLException{
        ObservableList<String> userNameList = FXCollections.observableArrayList();
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "SELECT userName FROM user;";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next())
            userNameList.add(rs.getString("userName"));

        return userNameList;
    }
}
