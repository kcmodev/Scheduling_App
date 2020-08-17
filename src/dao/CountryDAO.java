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

public class CountryDAO {
    private static final Connection conn = ConnectionHandler.startConnection();

    /**
     * returns country name based on city ID
     * @param cityId
     * @return
     * @throws SQLException
     */
    public static String getCountryName(int cityId) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select cty.cityId, cntry.country from city cty\n" +
                "join country cntry on cty.countryId = cntry.countryId\n" +
                "where cityId = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setInt(1, cityId);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getString("country");

        return "";
    }
}
