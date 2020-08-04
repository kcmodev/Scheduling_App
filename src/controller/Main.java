package controller;

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
        primaryStage.setTitle("Christensen Software 2 PA");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        ConnectionHandler.startConnection();

        launch(args);
        
        ConnectionHandler.closeConnection(); // closes connection when program is closed
    }
}
