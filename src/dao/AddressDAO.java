package dao;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDAO {
    private static String sqlStatement;

    public static void addNewAddress(String address, int cityId, String postalCode, String phone) throws SQLException {
        sqlStatement = "insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)\n" +
                "values (?, ' ', ?, ?, ?, CURRENT_TIMESTAMP, 'admin', 'admin')";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, address);
        StatementHandler.getPreparedStatement().setInt(2, cityId);
        StatementHandler.getPreparedStatement().setString(3, postalCode);
        StatementHandler.getPreparedStatement().setString(4, phone);

        StatementHandler.getPreparedStatement().execute();
    }

    public static int getAddressId(String address) throws SQLException {
        int addressId = 0;

        sqlStatement = "select addressId, address from address where address = ?";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, address);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()) {
            addressId = rs.getInt("addressId");
        }
        return addressId;
    }
}
