package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryHandler {

    /**
     * create statement reference
     */
    private static PreparedStatement preparedStatement;

    /**
     * method to create statement object
     */
    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sqlStatement);
    }

    /**
     * getter to return statement object
     */
    public static PreparedStatement getPreparedStatement(){
        return preparedStatement;
    }
}
