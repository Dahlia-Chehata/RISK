package view;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class StartMenuController implements Initializable {


    static Stage p = null;
    public void startApplication(Stage primaryStage,String file) throws IOException, InterruptedException {
        if(p == null){
            p = primaryStage;
        }
        AnchorPane root = FXMLLoader.<AnchorPane>load(getClass().getResource("../Resources/" + file));
        primaryStage.setScene(new Scene(root, 900, 575));
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    @FXML
    private ChoiceBox<?> agentOne;

    @FXML
    private ChoiceBox<?> agentTwo;

    @FXML
    private Button simulationButton1;

    @FXML
    private ChoiceBox<?> intelligentAgent;

    @FXML
    private Button simulationButton2;

    @FXML
    void startSimulation(ActionEvent event) throws IOException, InterruptedException {
        Graph graph = new SingleGraph("Tutorial 1");
        FxViewer v = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        v.enableAutoLayout();
        FxViewPanel graphPanel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
        AnchorPane root = FXMLLoader.<AnchorPane>load(getClass().getResource("../Resources/graph.fxml"));
        root.getChildren().setAll(graphPanel);
        p.setScene(new Scene(root, 900, 575));
    }


    public static Stage getStage(){
        return p;
    }
}
