package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class WindowManager {

    public static final String LOGIN_SCREEN_TITLE = "Christensen Software 2 PA - Login Screen";
    public static final String MANAGE_APPOINTMENTS_WINDOW_TITLE = "Scheduled appointments";
    public static final String UPDATE_APPOINTMENT_TITLE = "Update appointment details";
    public static final String ADD_APPOINTMENT_TITLE = "Add new appointment";
    public static final String MANAGE_CUSTOMERS_TITLE = "Manage customer data";
    public static final String UPDATE_CUSTOMER_TITLE = "Update customer info";
    public static final String ADD_CUSTOMER_TITLE = "Add new customer";

    public void windowController(ActionEvent event, String fileName, String windowTitle) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fileName));
            Scene scene = new Scene(parent);
            Stage newWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.setTitle(windowTitle);
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load new window");
            System.exit(-1);
        }
    }
}
