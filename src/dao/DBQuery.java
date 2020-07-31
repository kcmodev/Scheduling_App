package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQuery {

    /**
     * create statement reference
     */
    private static Statement statement;

    /**
     * create statement object
     */
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    /**
     * getter to return statement object
     */
    public static Statement getStatement(){
        return statement;
    }
}
