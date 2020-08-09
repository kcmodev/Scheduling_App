package dao;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDAO {
    private static String sqlStatement;
    private static ResultSet rs;

    /**
     * method adds new address to database
     * @param address
     * @param cityId
     * @param zip
     * @param phone
     * @throws SQLException
     */
    public static void addNewAddress(String address, int cityId, String zip, String phone) throws SQLException {

        /**
         * checks for duplicates in database before proceeding to add a new record to address
         */
        System.out.println("new address? " + isNewAddress(address, cityId, zip, phone));
        if (isNewAddress(address, cityId, zip, phone)) {
            System.out.println("new address. adding \"" + address + "\" to the database");
            sqlStatement = "insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)\n" +
                    "values (?, ' ', ?, ?, ?, CURRENT_TIMESTAMP, 'admin', 'admin')";

            StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
            StatementHandler.getPreparedStatement().setString(1, address);
            StatementHandler.getPreparedStatement().setInt(2, cityId);
            StatementHandler.getPreparedStatement().setString(3, zip);
            StatementHandler.getPreparedStatement().setString(4, phone);
            StatementHandler.getPreparedStatement().execute();
        } else {
            System.out.println("address \"" + address + "\" already exists");
        }
    }

    public static int getAddressId(String address) throws SQLException {
        int addressId = 0;

        sqlStatement = "select addressId, address from address where address = ?";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, address);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()) {
            addressId = rs.getInt("addressId");
        }
        return addressId;
    }

    public static boolean isNewAddress(String address, int cityId, String zip, String phone) throws SQLException {
        sqlStatement = "select address, cityId, postalCode, phone from address\n" +
                        "where address = ? and cityId = ? and postalCode = ? and phone = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, address);
        StatementHandler.getPreparedStatement().setInt(2, cityId);
        StatementHandler.getPreparedStatement().setString(3, zip);
        StatementHandler.getPreparedStatement().setString(4, phone);

        rs = StatementHandler.getPreparedStatement().executeQuery();

        if (rs.next())
            return false;

        return true;
    }

}
