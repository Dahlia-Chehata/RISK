package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.Controller;
import Interfaces.IRiskGame;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import static java.lang.System.exit;


public class GraphMenuController {

    private Stage ps = null;
    private Graph graph = null;
    private Controller con = null;
    private List<Integer> player_0_state;
    private List<Integer> player_1_state;

    public GraphMenuController(Controller control) {
        con = control;
        player_0_state = new ArrayList<Integer>();
        player_1_state = new ArrayList<Integer>();
    }

    private <E> E chooseElement(List<E> listOfElement, String title, String headertext, String contentText) {
        ChoiceDialog<E> dialog = new ChoiceDialog<>(listOfElement.get(0), listOfElement);
        dialog.setTitle(title);
        dialog.setHeaderText(headertext);
        dialog.setContentText(contentText);
        Optional<E> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public void takeHumanMove(List<Integer> firstPlayer, List<Integer> secondPlayer) {
        Integer troopsIn = chooseElement(firstPlayer, "Insert Troops", "Select from your countries", "Choose Country:");
        /*
         * to add new troops
         * */
        con.game.set_cp_soldiers(troopsIn);
        Integer attacker = chooseElement(firstPlayer, "Attack", "Select country", "Choose Country to attack with:");
        /*
         * it means that in the current turn the player don't want to attack the other player.
         * */
        if (attacker == null) {
            return;
        }
        /*
         * choose the country to attack
         * */
        Integer attacked = chooseElement(secondPlayer, "Take", "Select country", "Choose Country to attack:");
        int totalTrops = con.game.get_country_soldiers(attacker) -
                con.game.get_country_soldiers(attacked);
        if (totalTrops < 2) {
            Alert cannotAttack = new Alert(Alert.AlertType.ERROR);
            cannotAttack.setHeaderText(null);
            cannotAttack.setContentText("Cannot attack this country");
            cannotAttack.showAndWait();
            return;
        }
        List<String> distributeTroops = new ArrayList<>();
        for (int i = 1; i < totalTrops; ++i) {
            distributeTroops.add(new String(Integer.toString(i) + "-" + Integer.toString(totalTrops - i)));
        }
        String distribution = chooseElement(distributeTroops, "put", "distribute after attack", "Choose distribution");
        String[] solidersInEachCountry = distribution.split("-");
        /*
         * to distribute the new troops between the two countries
         * */
        con.game.cp_attack(attacker, attacked,
                Integer.parseInt(solidersInEachCountry[0]), Integer.parseInt(solidersInEachCountry[1]));
    }

    public void showGraph(Stage primaryStage) throws IOException, InterruptedException {
        if (ps == null) {
            ps = primaryStage;
        }
        con.game.start_game();

        /*
         * start game as fars need
         * */
        AnchorPane root = FXMLLoader.load(getClass().getResource("../Resources/StartMenu.fxml"));

        graph = new SingleGraph("risk");
        String styleSheet =
                "node {" +
                        "	fill-color: black;" +
                        "}" +
                        "node.marked {" +
                        "	fill-color: red;" +
                        "}";
        graph.setAttribute("ui.stylesheet", styleSheet);
        FxViewer v = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        v.enableAutoLayout();
        FxViewPanel graphPanel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
        graphPanel.resize(root.getPrefWidth(), root.getPrefHeight());
        graphPanel.setPrefWidth(root.getPrefWidth());
        graphPanel.setPrefHeight(root.getPrefHeight());
        root.getChildren().clear();
        root.getChildren().add(graphPanel);
        Scene s = new Scene(root);
        primaryStage.setScene(s);
        drawGraph(con.game);
        player_0_state = con.game.get_player_countries(0);
        player_1_state = con.game.get_player_countries(1);
       // simulate();
        declareResult();
    }

    private void updateGraph(IRiskGame game) {
        List<Integer> player_0_new_state = con.game.get_player_countries(0);
        List<Integer> player_1_new_state = con.game.get_player_countries(1);
        for(Integer coun : player_0_state){
            boolean taken = false;
            for(Integer enemyCoun : player_1_new_state){
                if(enemyCoun.equals(coun)){
                    taken = true;
                    break;
                }
            }
            if(taken){
                Node v = graph.getNode(Integer.toString(coun));
                v.setAttribute("ui.class","one");
            }
        }
        for(Integer coun : player_1_state){
            boolean taken = false;
            for(Integer enemyCoun : player_0_new_state){
                if(enemyCoun.equals(coun)){
                    taken = true;
                    break;
                }
            }
            if(taken){
                Node v = graph.getNode(Integer.toString(coun));
                v.setAttribute("ui.class","zero");
            }
        }

        player_0_state = player_0_new_state;
        player_1_state = player_1_new_state;
    }

    private void simulate() {
        int currentPlayer = 0;
        while (!con.game.is_game_end()) {
            if (con.isHuman[currentPlayer])
                takeHumanMove(con.game.get_player_countries(currentPlayer),
                        //change here
                        con.game.get_player_countries(1 - currentPlayer));
            else
                con.simualtor.SimulateSingleStep2Agents();
            updateGraph(con.game);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            //toggle the player
            currentPlayer = 1 - currentPlayer;
            con.game.end_turn();
        }
    }

    private void declareResult() {
        simulate();
        Alert declare_winner = new Alert(Alert.AlertType.INFORMATION);
        declare_winner.setTitle("The Result");
        declare_winner.setHeaderText(null);
        declare_winner.setContentText("The winner is player : " + con.game.get_winning_player());
        declare_winner.showAndWait();
    }

    private void goToResultMenu() throws IOException {
        new ResultMenuController().showResult(ps);
    }

    /*
     *
     * */
    private void drawGraph(IRiskGame game) {

        //add first player nodes
        addNodes(game, 0, "zero");
        //add second player nodes
        addNodes(game, 1, "one");
        //connect edges
        char edges = 'A';
        for (Node v : graph) {
            for (Integer neigId : game.get_country_neighbours(Integer.parseInt(v.getId()))) {
                Node neighbourNode = graph.getNode(Integer.toString(neigId));
                if (!v.hasEdgeBetween(neighbourNode)) {
                    graph.addEdge(String.valueOf(edges++), v, neighbourNode);
                }
            }
        }
    }

    private void addNodes(IRiskGame game, int playerId, String classInCss) {
        for (Integer country : game.get_player_countries(playerId)) {
            Node newOne = graph.addNode(Integer.toString(country));
            newOne.setAttribute("ui.class", classInCss);
        }
    }

}

