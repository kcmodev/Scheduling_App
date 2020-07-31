package controller;

import dao.DBConnection;
import dao.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../gui/Login.fxml"));
        primaryStage.setTitle("Christensen Software 2 PA");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
//        Connection conn = DBConnection.startConnection();
//
//        DBQuery.setStatement(conn);
//        Statement statement = DBQuery.getStatement();
//        String insertStatement = "INSERT INTO country (country, createDate, createdBy, lastUpdateBy) " +
//                "VALUES ('US', '2020-07-31 00:00:00', 'admin', 'admin');";
//
//        statement.execute(insertStatement);
//        if (statement.getUpdateCount() > 0)
//            System.out.println("rows effected: " + statement.getUpdateCount());
//        else
//            System.out.println("no change");
//

        launch(args);

//        DBConnection.closeConnection();
    }
}
