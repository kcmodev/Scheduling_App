package ErrorHandling;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class PopupHandlers {

    private static String titleText;
    private static String headerText;

    /**
     * confirmation alert popup handler
     * passes in string variable to define text in the alert
     */
    public boolean confirmationAlert(String action){
        titleText = "Confirmation";
        headerText = "Please confirm";

        /**
         * instantiate alert popup for exiting the program
         */
        Alert exiting = new Alert(Alert.AlertType.CONFIRMATION);
        exiting.setTitle(titleText);
        exiting.setHeaderText(headerText);
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
    public void errorAlert(int errorCode, String errorText){
        headerText = "Error";

        if (errorCode == 1){ // no selection made
            titleText = "No selection made";
        }else if (errorCode == 2){ // invalid input
            titleText = "Invalid input";
        }

        Alert invalidChoice = new Alert(Alert.AlertType.ERROR);
        invalidChoice.setHeaderText(headerText);
        invalidChoice.setTitle(titleText);
        invalidChoice.setContentText(errorText);
        invalidChoice.showAndWait();
    }
}
