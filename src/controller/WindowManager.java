package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class WindowManager{

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
//
//    public void windowWithDataController(ActionEvent, String fileName, String windowTitle) {
//
//    }

//    private static WindowManager _instance;
//
//    public static WindowManager create(Stage stage, ResourceBundle bundle) throws IOException {
//        _instance = new WindowManager(stage, bundle);
//        return getInstance();
//    }
//
//    public static WindowManager getInstance() {
//        return _instance;
//    }
//
//    private WindowManager(Stage stage, ResourceBundle bundle) throws IOException {
//        // private constructor called only once
//        this.loadScene(“loginController”);
//    }
//
//
//    public void loadScene(String name) throws IOException {
//        FXMLLoader loader = new FXMLLoader();
//
//        loader.setLocation(getClass().getClassLoader().getResource(String.format("ui/%s.fxml", name)));
//        loader.setResources(this.bundle);
//        Scene scene = new Scene(loader.load());
//        Controller ctrlr = loader.getController();
//        this.scenes.put(name, new SceneInfo(scene, ctrlr));
//    }

}
