package view;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import Controller.Controller;
import Interfaces.IRiskGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;


public class GraphMenuController {

    private Stage ps = null;
    private Graph graph = null;
    private Controller con = null;

    public GraphMenuController(Controller control){
        con = control;

    }

    public  List<String> takeHumanMove(List<String> firstPlayer, List<String> secondPlayer, int addtionalTroops){
        List<String> result = new ArrayList<String>();
        ChoiceDialog<String> dialog1 = new ChoiceDialog<>(firstPlayer.get(0), firstPlayer);
        dialog1.setTitle("Insert Troops");
        dialog1.setHeaderText("Select from your countries");
        dialog1.setContentText("Choose Country:");
        Optional<String> result1 = dialog1.showAndWait();
        if (result1.isPresent()){
            result.add(new String(result1.get()));
        }
        ChoiceDialog<String> dialog2 = new ChoiceDialog<>(secondPlayer.get(0), secondPlayer);
        dialog2.setTitle("Select Countory to Attack");
        dialog2.setHeaderText("Select from your enemys\' countries");
        dialog2.setContentText("Choose Country:");
        Optional<String> result2 = dialog2.showAndWait();
        if (result2.isPresent()){
            result.add(new String(result2.get()));
            //get total trops in[mycountry] - in[enemyconuntry]
            int totalTrops = 10;
            List<String> distributeTroops = new ArrayList<>();
            for(int i = 1 ; i < totalTrops ; ++i){
                distributeTroops.add(new String(Integer.toString(i) + " - " + Integer.toString(totalTrops - i)));
            }
            ChoiceDialog<String> dialog3 = new ChoiceDialog<>(distributeTroops.get(0), distributeTroops);
            dialog3.setTitle("distribute your troops");
            dialog3.setHeaderText("set your troops");
            dialog3.setContentText("choose: ");
            Optional<String> result3 = dialog3.showAndWait();
            if(result3.isPresent()){
                result.add(new String(result3.get()));
            }
        }
        return result;
    }

    public void showGraph(Stage primaryStage) throws IOException, InterruptedException {
        if(ps == null){
            ps = primaryStage;
        }
        updateGraph(con.game);
        FxViewer v = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        v.enableAutoLayout();
        FxViewPanel graphPanel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
        AnchorPane root = FXMLLoader.<AnchorPane>load(getClass().getResource("../Resources/graph.fxml"));
        root.getChildren().setAll(graphPanel);
        ps.setScene(new Scene(root, 900, 575));
        while(true){
            con.simualtor.firstMakeStep();
            if(con.game.is_game_end()){
                break;
            }
            updateGraph(con.game);
            try { Thread.sleep(1000); } catch (Exception e) {}
            con.simualtor.secondMakeStep();
            if(con.game.is_game_end()){
                break;
            }
            updateGraph(con.game);
        }
        goToResultMenu();
    }

    public void goToResultMenu() throws IOException {
        new ResultMenuController().showResult(ps);
    }
    /*
    *
    * */
    private void updateGraph(IRiskGame game){
        graph = new SingleGraph("Risk Simulator");
        String styleSheet =
                "zero {" +
                        "	fill-color: red;" +
                        "}" +
                        "one {" +
                        "	fill-color: blue;" +
                        "}";
        graph.setAttribute("ui.stylesheet", styleSheet);
        //add first player nodes
        addNodes(game,0,"zero");
        //add second player nodes
        addNodes(game,1,"one");
        //connect edges
        for(Node v : graph){
            for(Integer neigId : game.get_country_neighbours(Integer.parseInt(v.getId()))){
                Node neighbourNode = graph.getNode(Integer.toString(neigId));
                if(!v.hasEdgeBetween(neighbourNode)){
                    graph.addEdge(v.getId() + neighbourNode.getId(),v,neighbourNode);
                }
            }
        }
    }
    private void addNodes(IRiskGame game,int playerId,String classInCss){
        for(Integer country : game.get_player_countries(playerId)){
            Node newOne = graph.addNode(Integer.toString(country));
            newOne.setAttribute("ui.class", classInCss);
            newOne.setAttribute("ui.label",Integer.toString(game.get_country_soldiers(country)));
        }
    }

}
