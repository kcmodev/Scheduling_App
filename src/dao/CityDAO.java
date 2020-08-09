package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.City;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityDAO {
    private static ObservableList<City> allCities = FXCollections.observableArrayList();
    private static ObservableList<String> cityNames = FXCollections.observableArrayList();
    private static String sql;

    public static void setAllCities() throws SQLException {
        sql = "select cityId, city, countryId from city;";

        StatementHandler.setPreparedStatement(ConnectionHandler.connection, sql);
        ResultSet rs = StatementHandler.getPreparedStatement().executeQuery();

        while (rs.next()){
            int cityId = rs.getInt("cityId");
            String cityName = rs.getString("city");
            int countryId = rs.getInt("countryId");

            City city = new City(cityId, cityName, countryId);
            allCities.add(city);
            cityNames.add(cityName);
        }

        System.out.println("List of cities added: ");
        for (City city : allCities)
            System.out.println(city.getCityName());
    }

    public static ObservableList<City> getAllCities(){ return allCities; }

    public static ObservableList<String> getCityNames(){ return cityNames; }
}
