package controller;

import ErrorHandling.PopupHandlers;
import ErrorHandling.LoginError;
import dao.AppointmentDAO;
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

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginController implements Initializable {
    /**
     * gets user locale
     * comment first and uncomment second to check locale requirements
     */
    private static final Locale userLocale = Locale.getDefault();
    private static final WindowManager window = new WindowManager();
//    private static final Locale userLoc = new Locale("es_CL"); // set to spanish to check locale settings

    /**
     * gets user timezone
     * comment first and uncomment second to check timezone requirements
     */
    private static LocalDateTime dateTime = LocalDateTime.now();
    private static ZoneId userSystemZone = ZoneId.systemDefault();
    private static TimeZone userTimeZone = TimeZone.getTimeZone(userSystemZone);
    private static ZonedDateTime localToZoned = dateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
    private static BufferedWriter writer;


    private static final PopupHandlers popups = new PopupHandlers();
    private static AppointmentDAO appointmentData = new AppointmentDAO();
    private static UserDAO userData = new UserDAO();
    private static ResourceBundle languageSetting;
    public static String currentUser;
    private static String logText;


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


    /**
     * handles clicking log in button
     * @param event
     */
    public void logInHandler(ActionEvent event) throws SQLException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy/MM/dd HH:mm:ss");
        String enteredUserName = userNameTextField.getText();
        String enteredPassword = passwordTextField.getText();

//        window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
        /**
         * checks login credentials
         */
        try {

            /**
             * checks to make sure user name and/or password are not left blank
             */
            if (enteredUserName.equals("") || enteredPassword.equals("")){
                System.out.println("found a blank field");
                throw new LoginError(languageSetting.getString("noInput"));
            /**
             * checks username for alphanumeric characters and that user exists in database
             */
            } else {
                if (enteredUserName.matches("^[a-zA-Z0-9]*$") && userData.isUser(enteredUserName)) { // username exists
                    if (enteredPassword.matches("^[a-zA-Z0-9]*$") && userData.passwordMatch(enteredUserName, enteredPassword)) { // password matches username
                        currentUser = enteredUserName;
                        logText = "user \"" + currentUser + "\" logged in successfully at " + localToZoned.toLocalDateTime().format(formatter)
                                + " hrs" + System.getProperty("line.separator");
                        writer.write(logText);
                        writer.flush();
                        writer.close();

                        window.windowController(event, "/gui/ManageAppointments.fxml", WindowManager.MANAGE_APPOINTMENTS_WINDOW_TITLE);
                    } else {
                        logText = "user \"" + enteredUserName + "\" unsuccessful login attempt. Password mismatch at " + localToZoned.toLocalDateTime().format(formatter)
                                + " hrs" + System.getProperty("line.separator");
                        writer.write(logText);
                        writer.flush();
                        throw new LoginError(languageSetting.getString("invalidPass"));
                    }
                } else {
                    logText = "user \"" + enteredUserName + "\" unsuccessful login attempt. Invalid user at " + localToZoned.toLocalDateTime().format(formatter)
                            + " hrs" + System.getProperty("line.separator");
                    writer.write(logText);
                    writer.flush();
                    throw new LoginError(languageSetting.getString("invalidUser"));
                }
            }
        } catch (LoginError e) {
            Alert invalidChoice = new Alert(Alert.AlertType.ERROR);
            invalidChoice.setHeaderText(languageSetting.getString("invalidLogin"));
            invalidChoice.setTitle(languageSetting.getString("titleLabel"));
            invalidChoice.setContentText(e.getLocalizedMessage());
            invalidChoice.showAndWait();

        } catch (IOException f){
            f.printStackTrace();
        }
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

        try {
            File file = new File("LogData.txt");
            if (!file.exists())
                file.createNewFile();

            FileWriter fileWriter = new FileWriter(file, true);
            writer = new BufferedWriter(fileWriter);
        } catch (IOException e){

        }

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
