package dao;

import controller.WindowManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.City;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityDAO {
    private static final Connection conn = ConnectionHandler.startConnection();

    private static ObservableList<City> allCities = FXCollections.observableArrayList();
    private static ObservableList<String> cityNames = FXCollections.observableArrayList();

    public int getCityId(String cityName) throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select cityId, city from city\n" +
                "where city = ?;";

        statement.setPreparedStatement(conn, sqlStatement);
        statement.getPreparedStatement().setString(1, cityName);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

        if (rs.next())
            return rs.getInt("cityId");

        return 0;
    }

    public ObservableList<String> getCityNames(){ return cityNames; }

    /**
     * build static list of city data
     * @throws SQLException
     */
    public static void buildListOfCities() throws SQLException {
        StatementHandler statement = new StatementHandler();
        String sqlStatement = "select cityId, city, countryId from city;";

        statement.setPreparedStatement(conn, sqlStatement);
        ResultSet rs = statement.getPreparedStatement().executeQuery();

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
