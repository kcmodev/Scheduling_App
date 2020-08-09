package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CountryDAO {
    private static String sqlStatement = "";
    private static ResultSet rs;

    public static String getCountryName(int cityId) throws SQLException {
        sqlStatement = "select cty.cityId, cntry.country from city cty\n" +
                "join country cntry on cty.countryId = cntry.countryId\n" +
                "where cityId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, cityId);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getString("country");

        return "";
    }
}
