package view;


import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class StartMenuController{


    public void startApplication(Stage stage) throws IOException {
        AnchorPane root = FXMLLoader.<AnchorPane>load(getClass().getResource("sample.fxml"));
        stage.setTitle("Risk Simulator");
        stage.setScene(new Scene(root, 300, 275));
        stage.show();
    }
}
