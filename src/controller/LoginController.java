package controller;

import dao.AppointmentDAO;
import dao.ConnectionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

        window.windowController(event, "/gui/ManageAppointments.fxml", WindowManager.MANAGE_APPOINTMENTS_WINDOW_TITLE);

        /**
         * checks to make sure user name and/or password are not left blank
         */
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
//                StatementHandler.setPreparedStatement(ConnectionHandler.startConnection(), "SELECT * FROM user");
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
//                                loader.setLocation(getClass().getResource("/gui/ManageAppointments.fxml"));
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
//                            ConnectionHandler.closeConnection();
//                            PopupHandlers.errorAlert(1, "Username and password do not match");
//                            break;
//                        }
//                    } else {
//                        ConnectionHandler.closeConnection();
//                        PopupHandlers.errorAlert(1, "Invalid username");
//                        break;
//                    }
//                }
//            } else {
//                PopupHandlers.errorAlert(1, "Input must be alphanumeric");
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
//        Locale currentLoc = new Locale("es_CL"); // set to spanish to check locale settings
        System.out.println("Current locale: " + currentLoc);

        ResourceBundle languageSetting;
        if (currentLoc.toString().equals("en_US")){
            languageSetting = ResourceBundle.getBundle("resources/English");
        } else {
            languageSetting = ResourceBundle.getBundle("resources/Spanish");
        }
        loginTitleLabel.setText(languageSetting.getString("titleLabel"));
        loginUserLabel.setText(languageSetting.getString("userNameLabel"));
        loginPasswordLabel.setText(languageSetting.getString("passwordLabel"));
    }
}
