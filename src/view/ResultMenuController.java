package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class ResultMenuController {

    @FXML
    private Button backButton;

    @FXML
    private Button exitButton;

    @FXML
    void exitAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void goBackAction(ActionEvent event) throws IOException, InterruptedException {
        new StartMenuController().startApplication(StartMenuController.getStage(),"StartMenu.fxml");
    }

}
