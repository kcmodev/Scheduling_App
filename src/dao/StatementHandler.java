/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementHandler {

    /**
     * create statement reference
     */
    private PreparedStatement preparedStatement;

    /**
     * method to create statement object
     */
    public void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sqlStatement);
    }

    /**
     * getter to return statement object
     */
    public PreparedStatement getPreparedStatement(){
        return preparedStatement;
    }
}
