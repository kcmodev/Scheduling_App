package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDAO {
    private static final Connection conn = ConnectionHandler.startConnection();

    /**
     * method adds new address to database
     * @param address
     * @param cityId
     * @param zip
     * @param phone
     * @throws SQLException
     */
    public void addNewAddress(String address, int cityId, String zip, String phone) throws SQLException {
        StatementHandler statement = new StatementHandler();

        String sqlStatement = "insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)\n" +
                "values (?, ' ', ?, ?, ?, NOW(), 'admin', 'admin')";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, address);
        statement.getPreparedStatement().setInt(2, cityId);
        statement.getPreparedStatement().setString(3, zip);
        statement.getPreparedStatement().setString(4, phone);
        statement.getPreparedStatement().execute();
}

    /**
     * return address Id using address string
     * @param address
     * @return
     * @throws SQLException
     */
    public int getAddressId(String address) throws SQLException {
        StatementHandler statement = new StatementHandler();
        int addressId = 0;
        String sqlStatement = "select addressId, address from address where address = ?";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, address);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        while (rs.next()) {
            addressId = rs.getInt("addressId");
        }
        return addressId;
    }

    /**
     * method checks for a unique address
     * @param address
     * @param cityId
     * @param zip
     * @param phone
     * @return
     * @throws SQLException
     */
    public boolean isNewAddress(String address, int cityId, String zip, String phone) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select address, cityId, postalCode, phone from address\n" +
                        "where address = ? and cityId = ? and postalCode = ? and phone = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, address);
        statement.getPreparedStatement().setInt(2, cityId);
        statement.getPreparedStatement().setString(3, zip);
        statement.getPreparedStatement().setString(4, phone);

        ResultSet rs = statement.getPreparedStatement().executeQuery();

        if (rs.next())
            return false;

    return true;
    }

    public void deleteUnownedAddress(int addressId){ }
}
