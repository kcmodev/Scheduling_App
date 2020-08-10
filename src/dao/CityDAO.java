package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.City;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityDAO {
    private static ObservableList<City> allCities = FXCollections.observableArrayList();
    private static ObservableList<String> cityNames = FXCollections.observableArrayList();
    private static String sqlStatement;
    private static ResultSet rs;

    public static int getCityId(String cityName) throws SQLException {
        sqlStatement = "select cityId, city from city\n" +
                "where city = ?;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        StatementHandler.getPreparedStatement().setString(1, cityName);
        rs = StatementHandler.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getInt("cityId");

        return 0;
    }

    public static ObservableList<String> getCityNames(){ return cityNames; }

    /**
     * build static list of city data
     * @throws SQLException
     */
    public static void buildListOfCities() throws SQLException {
        sqlStatement = "select cityId, city, countryId from city;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sqlStatement);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            int cityId = rs.getInt("cityId");
            String cityName = rs.getString("city");
            int countryId = rs.getInt("countryId");

            City city = new City(cityId, cityName, countryId);
            allCities.add(city);
            cityNames.add(cityName);
        }
    }
}
