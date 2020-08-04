package controller;

import dao.ConnectionHandler;
import dao.CustomerDAO;
import dao.StatementHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML private TextField userNameTextField;
    @FXML private PasswordField passwordTextField;

    @FXML private Label loginTitleLabel;
    @FXML private Label loginUserLabel;
    @FXML private Label loginPasswordLabel;

    /**
     * instantiates window manager and connection to database
     */
    WindowManager window = new WindowManager();
    Connection connection = ConnectionHandler.startConnection();

    /**
     * handles clicking log in button
     * @param event
     */
    public void logInHandler(ActionEvent event) throws SQLException {
        System.out.println("log in button clicked");
        String enteredUserName = userNameTextField.getText();
        String enteredPassword = passwordTextField.getText();
        System.out.println("User name entered: \"" + enteredUserName + "\"");
        System.out.println("Password entered: \"" + enteredPassword + "\"");

        CustomerDAO.buildCustomerData();
        window.windowController(event, "/gui/Appointments.fxml", AppointmentsController.APPOINTMENT_WINDOW_TITLE);

//        /**
//         * checks to make sure user name and/or password are not left blank
//         */
//        if (enteredUserName.equals("") || enteredPassword.equals("")){
//            System.out.println("found a blank field");
//            PopupHandlers.errorAlert(2, "Please enter a user name and password");
//        /**
//         * checks username for alphanumeric characters
//         */
//        } else {
//            if (enteredUserName.matches("^[a-zA-Z0-9]*$")) {
//
//                System.out.println("entered if statement");
//                StatementHandler.setPreparedStatement(connection, "SELECT * FROM user");
//                System.out.println("sql statement running");
//                PreparedStatement checkCreds = StatementHandler.getPreparedStatement();
//                ResultSet userList = checkCreds.executeQuery();
//
//                /**
//                 * parses user table to verify username and password match a record
//                 * in the database
//                 */
//                while (userList.next()) {
//                    boolean isValidUser = enteredUserName.equals(userList.getString("userName"));
//                    if (isValidUser) {
//                        System.out.println("user match found: \"" + userList.getString("userName") + "\"");
//                        boolean isValidPassword = enteredPassword.equals(userList.getString("password"));
//                        if (isValidPassword) {
//                            System.out.println("password matches. log in successful");
//                            try {
//                                FXMLLoader loader = new FXMLLoader();
//                                loader.setLocation(getClass().getResource("/gui/Appointments.fxml"));
//                                Parent parent= loader.load();
//                                Scene appointmentsScene = new Scene(parent);
//
//                                AppointmentsController controller = loader.getController();
//                                controller.setLabel("Schedule appointments for " + enteredUserName);
//
//                                Stage newWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                                newWindow.setScene(appointmentsScene);
//                                newWindow.setResizable(false);
//                                newWindow.setTitle(AppointmentsController.APPOINTMENT_WINDOW_TITLE);
//                                newWindow.show();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if (!isValidPassword) {
//                            System.out.println("password does not match");
//                        }
//                    }
//                }
//            } else {
//                System.out.println("input not alphanumeric");
//            }
//        }
    }

    /**
     * handles exit button click
     */
    public void exitHandler (){
        if (PopupHandlers.confirmationAlert("exit the program")) {
            ConnectionHandler.closeConnection();
            System.exit(1);
        }
    }

    /**
     * initialized log in screen with the labels set to the correct language
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Locale currentLoc = Locale.getDefault();
        System.out.println("Current locale: " + currentLoc);

        ResourceBundle languageSetting;
        if (currentLoc.toString().equals("en_US")){
            languageSetting = ResourceBundle.getBundle("English");
        } else {
            languageSetting = ResourceBundle.getBundle("Spanish");
        }
        loginTitleLabel.setText(languageSetting.getString("titleLabel"));
        loginUserLabel.setText(languageSetting.getString("userNameLabel"));
        loginPasswordLabel.setText(languageSetting.getString("passwordLabel"));

    }
}
