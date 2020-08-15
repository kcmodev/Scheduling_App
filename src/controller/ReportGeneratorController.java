package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

public class ReportGeneratorController {
    private final WindowManager window = new WindowManager();

    @FXML
    private TableView reportTable;
    @FXML
    private TableColumn columnLeft;
    @FXML
    private TableColumn columnRight;

    public void appointmentsByMonth(){

    }

    public void consultantSchedule(){

    }

    public void appointmentsPerCustomer(){

    }

    public void backButtonClicked(ActionEvent event){
        window.windowController(event, "gui/ManageAppointmentsController.fxml", window.MANAGE_APPOINTMENTS_WINDOW_TITLE);
    }
}
