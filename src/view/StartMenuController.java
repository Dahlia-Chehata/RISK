package view;
import Controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;


public class StartMenuController extends Application implements Initializable {

    private  Controller con = null;
    static Stage ps = null;
    public void startApplication(Stage primaryStage,String file) throws IOException, InterruptedException {
        if(ps == null){
            ps = primaryStage;
        }
        AnchorPane root = FXMLLoader.<AnchorPane>load(getClass().getResource("../Resources/" + file));
        primaryStage.setScene(new Scene(root, 900, 575));
        primaryStage.setResizable(false);
        primaryStage.show();


    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        agentOne.getItems().addAll(Arrays.asList("Passive","Agressive","Pacifist","Human"));
        agentTwo.getItems().addAll(Arrays.asList("Passive","Agressive","Pacifist","Human"));
        intelligentAgent.getItems().addAll(Arrays.asList("Greedy","A star","RealTime A star"));
    }
    @FXML
    private ChoiceBox<String> agentOne;

    @FXML
    private ChoiceBox<String> agentTwo;

    @FXML
    private Button simulationButton1;

    @FXML
    private ChoiceBox<String> intelligentAgent;

    @FXML
    private Button simulationButton2;

    @FXML
    void startSimulation1(ActionEvent event) throws IOException, InterruptedException {
        con.simualtor.selectFirstAgent(agentOne.getValue());
        con.simualtor.selectSecondAgent(agentTwo.getValue());
        new GraphMenuController(con).showGraph(ps);
    }
    @FXML
    void startSimulation2(ActionEvent event) throws IOException, InterruptedException {
        con.simualtor.selectFirstAgent(intelligentAgent.getValue());
        con.simualtor.selectFirstAgent("passive");
        new GraphMenuController(con).showGraph(ps);
    }


    public static Stage getStage(){
        return ps;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        con = new Controller();
        if(con.readGraph() && con.readPlayer(0) && con.readPlayer(1))
            new StartMenuController().startApplication(primaryStage,"StartMenu.fxml");
    }
    public static void main(String[] args){
        launch(args);
    }

}

