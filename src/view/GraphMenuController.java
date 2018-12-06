package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.IntToDoubleFunction;

import Controller.Controller;
import Interfaces.IRiskGame;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.graphicGraph.stylesheet.Color;
import org.graphstream.ui.javafx.FxGraphRenderer;

import static java.lang.System.exit;
import static java.lang.System.setOut;


public class GraphMenuController {

    private Stage ps = null;
    private Graph graph = null;
    private Controller con = null;
    private static final String red = "fill-color: rgb(255,0,0);";
    private static final String blue = "fill-color: rgb(0,0,255);";


    public GraphMenuController(Controller control) {
        con = control;
    }

    private <E> E chooseElement(List<E> listOfElement, String title, String headertext, String contentText) {
        ChoiceDialog<E> dialog = new ChoiceDialog<>(listOfElement.get(0), listOfElement);
        dialog.setX(500);
        dialog.setY(500);
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
        if(troopsIn == null){
            return;
        }
        con.game.set_cp_soldiers(troopsIn);
        updateGraph(con.game);
        Integer attacker = chooseElement(firstPlayer, "Attack", "Select country", "Choose Country to attack with:");
        if (attacker == null) {
            return;
        }
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
        System.out.println(totalTrops);
        List<String> distributeTroops = new ArrayList<>();
        for (int i = 1; i < totalTrops; ++i) {
            distributeTroops.add(new String(Integer.toString(i) + "-" + Integer.toString(totalTrops - i)));
        }
        String distribution = chooseElement(distributeTroops, "put", "distribute after attack", "Choose distribution");
        String[] solidersInEachCountry = distribution.split("-");
        con.game.cp_attack(attacker, attacked,
                Integer.parseInt(solidersInEachCountry[0]), Integer.parseInt(solidersInEachCountry[1]));
    }

    public void showGraph(Stage primaryStage) throws IOException, InterruptedException {
        if (ps == null) {
            ps = primaryStage;
        }
        con.game.start_game();
        AnchorPane root = FXMLLoader.load(getClass().getResource("../Resources/StartMenu.fxml"));
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new SingleGraph("risk");
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
        simulate();
        declareResult();
    }

    private void updateGraph(IRiskGame game) {
        for(Integer coun : game.get_player_countries(0)){
            Node v = graph.getNode(Integer.toString(coun));
            v.setAttribute("ui.style",red);
            String build = Integer.toString(coun) + " - " + Integer.toString(game.get_country_soldiers(coun));
            v.setAttribute("ui.label", build);
        }
        for(Integer coun : game.get_player_countries(1)){
            Node v = graph.getNode(Integer.toString(coun));
            v.setAttribute("ui.style",blue);
            String build = Integer.toString(coun) + " - " + Integer.toString(game.get_country_soldiers(coun));
            v.setAttribute("ui.label", build);
        }
    }

    private void simulate() throws IOException {
        int currentPlayer = 0;
        boolean ended = false;
        while (!con.game.is_game_end()) {
            if (con.isHuman[currentPlayer])
                takeHumanMove(con.game.get_player_countries(currentPlayer),
                        con.game.get_player_countries(1 - currentPlayer));
            else {
                con.simualtor.SimulateSingleStep2Agents();
            }
            con.game.end_turn();
            currentPlayer = 1 - currentPlayer;
            updateGraph(con.game);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setX(500);
            alert.setY(500);
            alert.setTitle("Next Turn");
            alert.setContentText("Do you want to play the next turn ?");
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                System.out.println("the turn is played");
            } else {
                ended = true;
                break;
            }
        }
        if (ended == false) {
            declareResult();
        } else {
            goToResultMenu();
        }
    }

    private void declareResult() throws IOException {
        Alert declare_winner = new Alert(Alert.AlertType.INFORMATION);
        declare_winner.setTitle("The Result");
        declare_winner.setHeaderText(null);
        declare_winner.setContentText("The winner is player : " + con.game.get_winning_player());
        declare_winner.showAndWait();
        goToResultMenu();
    }

    private void goToResultMenu() throws IOException {
        new ResultMenuController().showResult(ps);
    }

    /*
     *
     * */
    private void drawGraph(IRiskGame game) {

        //add first player nodes
        addNodes(game, 0, red);
        //add second player nodes
        addNodes(game, 1, blue);
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
            newOne.setAttribute("ui.style", classInCss);
            String build = Integer.toString(country) + " - " + Integer.toString(game.get_country_soldiers(country));
            newOne.setAttribute("ui.label", build);
        }
    }

}

