package controller;

import dao.AppointmentDAO;
import dao.CityDAO;
import dao.ConnectionHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

public class Main extends Application {
    public static final TimeZone userZone = Calendar.getInstance().getTimeZone();
    private static AppointmentDAO appointmentData = new AppointmentDAO();

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../gui/Login.fxml"));
        primaryStage.setTitle(WindowManager.LOGIN_SCREEN_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
//        ConnectionHandler.startConnection();

        CityDAO.buildListOfCities();
        appointmentData.buildValidApptTimes();

        launch(args);
        
        ConnectionHandler.closeConnection(); // closes connection when program is closed
    }
}
