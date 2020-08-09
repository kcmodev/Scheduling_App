package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CountryDAO {
    private static String sqlStatement = "";
    private static ResultSet rs;

    public static String getCountryName(int customerId) throws SQLException {
        sqlStatement = "select c.customerId, cntry.country from customer c\n" +
                "join address a on c.addressId = a.addressId\n" +
                "join city cty on a.cityId = cty.cityId\n" +
                "join country cntry on cty.countryId = cntry.countryId\n" +
                "where customerId = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setInt(1, customerId);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getString("country");

        return "";
    }
}
