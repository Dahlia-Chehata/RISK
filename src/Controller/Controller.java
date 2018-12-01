package Controller;
import Interfaces.IRiskGame;
import Interfaces.ISimulate;
import Model.RiskGame;
import Simulator.Simulator;

import java.io.*;
import java.util.ArrayList;


public class Controller {
    public IRiskGame game;
    public ISimulate simualtor;
    public boolean[] isHuman;

    public Controller() throws IOException {
        game = new RiskGame();
        isHuman = new boolean[2];
        readGraph();
        readPlayer(0);
        readPlayer(1);
        simualtor = new Simulator();
        simualtor.SetGameObject(game);
    }

    /*
     * read only the graph
     * */
    public boolean readGraph() throws IOException {
        FileInputStream fstream = new FileInputStream("src/Resources/graphRead.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        boolean returnVal = true;
        int lineNumber = 1;
        int edgesCount = 0;
        int partitionsNumber = 0;
        while ((strLine = br.readLine()) != null) {
            String[] tokens = strLine.split(" ");
            if (lineNumber == 1) {
                returnVal |= game.set_count_of_countries(Integer.parseInt(tokens[1]));

            } else if (lineNumber == 2) {
                edgesCount = Integer.parseInt(tokens[1]);
            } else if (edgesCount > 0) {
                returnVal |= game.add_edge(Integer.parseInt(tokens[0]) - 1, Integer.parseInt(tokens[1]) - 1);
                edgesCount--;
            } else if (partitionsNumber > 0) {
                boolean firstNumber = true;
                int partitionBouns = 0;
                ArrayList<Integer> countries = new ArrayList<Integer>();
                for (String s : tokens) {
                    if (firstNumber == true) {
                        partitionBouns = Integer.parseInt(s);
                        firstNumber = false;
                    } else {
                        countries.add(new Integer(Integer.parseInt(s)) - 1);
                    }
                }
                returnVal |= game.add_partition(partitionBouns, countries);
                partitionsNumber--;
            } else {
                partitionsNumber = Integer.parseInt(tokens[1]);
                game.set_count_of_paritions(partitionsNumber);
            }
            lineNumber++;
        }
        in.close();
        return returnVal;
    }

    /*
     * this method only read the troops for the player
     * */
    public boolean readPlayer(int playerId) throws IOException {
        FileInputStream fstream = new FileInputStream("src/Resources/troops" + Integer.toString(playerId) + ".txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        boolean returnVal = true;
        while ((strLine = br.readLine()) != null) {
            String[] tokens = strLine.split(" ");
            returnVal |= game.add_soldiers(playerId, Integer.parseInt(tokens[0]) - 1, Integer.parseInt(tokens[1]));
        }
        in.close();
        return returnVal;
    }

}
