/**
 * Author: Steven Christensen
 * Email: schr206@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package controller;

import dao.ReportDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportGeneratorController implements Initializable {
    private final WindowManager window = new WindowManager();
    private static final ReportDAO reportData = new ReportDAO();
    private static final UserDAO userData = new UserDAO();

    @FXML private TextArea reportArea;
    @FXML private ComboBox<String> consultantList;

    /**
     * generates report to list out all appointments grouped by their respective months
     * uses a stream pipeline and lambda expression to append all the generated fields to the text area
     * which is view only
     * @throws SQLException
     */
    public void appointmentsByMonth() throws SQLException {
        reportArea.clear();
        reportData.getAppointmentsPerMonth()
                .stream()
                .forEachOrdered(x -> reportArea.appendText(x.toString()));
        consultantList.setValue(consultantList.getPromptText());

    }

    /**
     * generates report to list out all appointments for the selected user
     * uses a stream pipeline and lambda expression to append all the generated fields to the text area
     * which is view only
     * @throws SQLException
     */
    public void consultantSchedule() throws SQLException {
        System.out.println("combo box clicked");
        if (!consultantList.getValue().equals(consultantList.getPromptText())) {
            reportArea.clear();
            reportData.getAllAppointmentsForUser(consultantList.getValue())
                    .stream()
                    .forEachOrdered(x -> reportArea.appendText(x.toString()));
        }
    }

    /**
     * generates report to list out the number of appointments made by each customer
     * uses a stream pipeline and lambda expression to append all the generated fields to the text area
     * which is view only
     * @throws SQLException
     */
    public void appointmentsPerCustomer() throws SQLException {
        reportArea.clear();
        reportData.getAppointmentsPerCustomer()
                .stream()
                .forEachOrdered(x -> reportArea.appendText(x.toString()));
        consultantList.setValue(consultantList.getPromptText());
    }

    /**
     * handles back button clicks
     * @param event
     */
    public void backButtonClicked(ActionEvent event){
        window.windowController(event, "/gui/ManageAppointments.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reportArea.setText("Report appears here");

        try {
            consultantList.setItems(userData.getAllUsers());
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }
}
