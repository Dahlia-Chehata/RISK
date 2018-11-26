package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;


public class ResultMenuController {

    @FXML
    private Label winnerLabel;

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

    public void showResult(Stage ps) throws IOException {
        //text dialogue to show winner
        AnchorPane root = FXMLLoader.<AnchorPane>load(getClass().getResource("../Resources/ResultMenu.fxml"));
        ps.setScene(new Scene(root, 900, 575));

    }
}
