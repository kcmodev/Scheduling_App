package controller;

import dao.DBConnection;
import dao.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class LoginController {

    @FXML private TextField userNameTextField;
    @FXML private PasswordField passwordTextField;

    /**
     * instantiates window manager and connection to database
     */
    WindowManager window = new WindowManager();
    Connection conn = DBConnection.startConnection();

    private String userName, password, checkUser, checkPass;

    /**
     * handles clicking log in button
     * @param event
     */
    public void logInHandler(ActionEvent event) throws SQLException {
        System.out.println("log in button clicked");
        userName = userNameTextField.getText();
        password = passwordTextField.getText();

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        
//        checkUser = "";
//        checkPass = "";

//        statement.execute(insertStatement);
//        if (statement.getUpdateCount() > 0)
//            System.out.println("rows effected: " + statement.getUpdateCount());
//        else
//            System.out.println("no change");


        window.windowController(event, "/gui/Appointments.fxml", "Scheduled Appointments");
    }
}
