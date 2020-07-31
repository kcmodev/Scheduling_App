package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ErrorHandlers {

    private static String headerText, titleText, errorText;

    /**
     * confirmation alert popup handler
     * passes in string variable to define text in the alert
     */
    public static final boolean confirmationAlert(String action){
        /**
         * instantiate alert popup for exiting the program
         */
        Alert exiting = new Alert(Alert.AlertType.CONFIRMATION);
        exiting.setTitle("Confirmation");
        exiting.setHeaderText("Please confirm.");
        exiting.setContentText("Are you sure you would like to " + action + "?");

        /**
         * set method for user to choose to quit by waiting on button press
         */
        Optional<ButtonType> choice = exiting.showAndWait();

        if (choice.get() == ButtonType.OK) { // user clicks yes
            return true;

        } else if (choice.get() == ButtonType.CANCEL){ // user clicks no
            exiting.close();
        }
        return false;
    }

    /**
     * overloaded error alert popup handler to receive error code
     * and custom message to display in alert popup
     */
    public static final void errorAlert(int errorCode, String errorText){

        if (errorCode == 1){ // no selection made
            headerText = "Error";
            titleText = "No selection made";
            errorText = "You must make a selection";
        }
        if (errorCode == 2){ // invalid input
            headerText = "Error";
            titleText = "Invalid input";
        }

        Alert invalidChoice = new Alert(Alert.AlertType.ERROR);
        invalidChoice.setHeaderText(headerText);
        invalidChoice.setTitle(titleText);
        invalidChoice.setContentText(errorText);
        invalidChoice.showAndWait();
    }

    /**
     * error alert popup handler
     */
    public static final void errorAlert(int errorCode){

        if (errorCode == 1){ // no selection made
            headerText = "Error";
            titleText = "No selection made";
            errorText = "You must make a selection";
        }
        if (errorCode == 2){ // invalid input
            headerText = "Error";
            titleText = "Invalid input";
            errorText = "You must enter valid input";
        }

        Alert invalidChoice = new Alert(Alert.AlertType.ERROR);
        invalidChoice.setHeaderText(headerText);
        invalidChoice.setTitle(titleText);
        invalidChoice.setContentText(errorText);
        invalidChoice.showAndWait();
    }
}
