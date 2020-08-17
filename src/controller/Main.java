/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../gui/Login.fxml"));
        primaryStage.setTitle(WindowManager.LOGIN_SCREEN_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {

        /**
         * builds static lists of cities and combo box data
         */
        CityDAO.buildListOfCities();
        AppointmentDAO.setValidHours();
        AppointmentDAO.setValidMinutes();
        AppointmentDAO.setValidYears();
        AppointmentDAO.setValidMonths();
        AppointmentDAO.setValidDays();

        launch(args);
        
        ConnectionHandler.closeConnection(); // closes connection when program is closed
    }
}
