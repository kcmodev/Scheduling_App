package controller;

import ErrorHandling.PopupHandlers;
import ErrorHandling.LoginError;
import dao.ConnectionHandler;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginController implements Initializable {
    /**
     * gets user locale
     * comment first and uncomment second to check locale requirements
     */
    private static final Locale userLocale = Locale.getDefault();
//    private static final Locale userLoc = new Locale("es_CL"); // set to spanish to check locale settings

    /**
     * gets user timezone
     * comment first and uncomment second to check timezone requirements
     */
    private static LocalDateTime dateTime = LocalDateTime.now();
    private static ZoneId userSystemZone = ZoneId.systemDefault();
    private static TimeZone userTimeZone = TimeZone.getTimeZone(userSystemZone);
    private static ZonedDateTime localToZoned = dateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));


    private static final PopupHandlers popups = new PopupHandlers();
    private static UserDAO userData = new UserDAO();
    private static ResourceBundle languageSetting;
    public static String userName;

    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label loginTitleLabel;
    @FXML
    private Label loginUserLabel;
    @FXML
    private Label loginPasswordLabel;

    private static WindowManager window = new WindowManager();

    /**
     * handles clicking log in button
     * @param event
     */
    public void logInHandler(ActionEvent event) throws SQLException {
        userName = userNameTextField.getText();
        String enteredPassword = passwordTextField.getText();

        window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);

//        /**
//         * checks login credentials
//         */
//        try {
//            /**
//             * checks to make sure user name and/or password are not left blank
//             */
//            if (enteredUserName.equals("") || enteredPassword.equals("")){
//                System.out.println("found a blank field");
//                throw new LoginError(languageSetting.getString("noInput"));
//            /**
//             * checks username for alphanumeric characters and that user exists in database
//             */
//            } else {
//                if (enteredUserName.matches("^[a-zA-Z0-9]*$") && userData.isUser(enteredUserName)) {
//                    if (enteredPassword.matches("^[a-zA-Z0-9]*$") && userData.passwordMatch(enteredUserName, enteredPassword)) {
//                        System.out.println("password matches. log in successful");
//                        FXMLLoader loader = new FXMLLoader();
//                        loader.setLocation(getClass().getResource("/gui/ManageAppointments.fxml"));
//                        Parent parent = loader.load();
//                        Scene appointmentsScene = new Scene(parent);
//
//                        ManageAppointmentsController controller = loader.getController();
//                        controller.setLabel("Schedule appointments for " + enteredUserName);
//
//                        Stage newWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                        newWindow.setScene(appointmentsScene);
//                        newWindow.setResizable(false);
//                        newWindow.setTitle(WindowManager.MANAGE_APPOINTMENTS_WINDOW_TITLE);
//                        newWindow.show();
//                    } else {
//                        throw new LoginError(languageSetting.getString("invalidPass"));
//                    }
//                } else {
//                    throw new LoginError(languageSetting.getString("invalidUser"));
//                }
//            }
//        } catch (LoginError e) {
//            Alert invalidChoice = new Alert(Alert.AlertType.ERROR);
//            invalidChoice.setHeaderText(languageSetting.getString("invalidLogin"));
//            invalidChoice.setTitle(languageSetting.getString("titleLabel"));
//            invalidChoice.setContentText(e.getLocalizedMessage());
//            invalidChoice.showAndWait();
//
//        } catch (IOException f){
//
//        }
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
        System.out.println("user offset: " + userSystemZone.getId() + " || " + userTimeZone.getDisplayName() + " || " + localToZoned.getOffset());
        System.out.println("Current locale: " + userLocale);

        if (userLocale.toString().equals("en_US")){
            languageSetting = ResourceBundle.getBundle("resources/English");
        } else {
            languageSetting = ResourceBundle.getBundle("resources/Spanish");
        }

        /**
         * set labels based on user location
         */
        loginTitleLabel.setText(languageSetting.getString("titleLabel"));
        loginUserLabel.setText(languageSetting.getString("userNameLabel"));
        loginPasswordLabel.setText(languageSetting.getString("passwordLabel"));
    }
}
