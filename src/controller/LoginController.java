package controller;

import dao.AppointmentDAO;
import dao.ConnectionHandler;
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
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;


public class LoginController implements Initializable {
    private WindowManager window = new WindowManager();
    private PopupHandlers popups = new PopupHandlers();

    private final ConnectionHandler conn = new ConnectionHandler();
    private String enteredUserName;
//    protected String errorTitle;
//    protected String errorHeader;
//    protected String errorText;

    @FXML private TextField userNameTextField;
    @FXML private PasswordField passwordTextField;

    @FXML private Label loginTitleLabel;
    @FXML private Label loginUserLabel;
    @FXML private Label loginPasswordLabel;

    /**
     * handles clicking log in button
     * @param event
     */
    public void logInHandler(ActionEvent event) throws SQLException {
        StatementHandler statement = new StatementHandler();

        enteredUserName = userNameTextField.getText();
        String enteredPassword = passwordTextField.getText();

        System.out.println("User name: \"" + enteredUserName + "\"");
        System.out.println("Password: \"" + enteredPassword + "\"");

        window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);

//        /**
//         * checks to make sure user name and/or password are not left blank
//         */
//        if (enteredUserName.equals("") || enteredPassword.equals("")){
//            System.out.println("found a blank field");
//            popups.errorAlert(2, "Please enter a user name and password");
//        /**
//         * checks username for alphanumeric characters
//         */
//        } else {
//            if (enteredUserName.matches("^[a-zA-Z0-9]*$")) {
//                statement.setPreparedStatement(ConnectionHandler.startConnection(), "SELECT * FROM user");
//                ResultSet userList = statement.getPreparedStatement().executeQuery();
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
//                                loader.setLocation(getClass().getResource("/gui/ManageAppointments.fxml"));
//                                Parent parent= loader.load();
//                                Scene appointmentsScene = new Scene(parent);
//
//                                ManageAppointmentsController controller = loader.getController();
//                                controller.setLabel("Schedule appointments for " + enteredUserName);
//
//                                Stage newWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                                newWindow.setScene(appointmentsScene);
//                                newWindow.setResizable(false);
//                                newWindow.setTitle(WindowManager.MANAGE_APPOINTMENTS_WINDOW_TITLE);
//                                newWindow.show();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if (!isValidPassword) {
//                            ConnectionHandler.closeConnection();
//                            popups.errorAlert(1, "Username and password do not match");
//                            break;
//                        }
//                    } else {
//                        ConnectionHandler.closeConnection();
//                        popups.errorAlert(1, "Invalid username");
//                        break;
//                    }
//                }
//            } else {
//                popups.errorAlert(1, "Input must be alphanumeric");
//            }
//        }
    }

    public void getUserId (String userName){

    }

    /**
     * handles exit button click
     */
    public void exitHandler (){
        ConnectionHandler conn = new ConnectionHandler();
        if (popups.confirmationAlert("exit the program")) {
            conn.closeConnection();
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
        Locale userLoc = Locale.getDefault();
//        TimeZone userZone = Calendar.getInstance().getTimeZone();
//        Locale currentLoc = new Locale("es_CL"); // set to spanish to check locale settings
        System.out.println("Current locale: " + userLoc);

        ResourceBundle languageSetting;
        if (userLoc.toString().equals("en_US")){
            languageSetting = ResourceBundle.getBundle("resources/English");
        } else {
            languageSetting = ResourceBundle.getBundle("resources/Spanish");
        }


        loginTitleLabel.setText(languageSetting.getString("titleLabel"));
        loginUserLabel.setText(languageSetting.getString("userNameLabel"));
        loginPasswordLabel.setText(languageSetting.getString("passwordLabel"));
    }
}
