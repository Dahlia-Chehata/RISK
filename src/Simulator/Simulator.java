package Simulator;

import Model.*;
import Interfaces.IAgent;
import Interfaces.IRiskGame;
import Interfaces.ISimulate;
import java.util.ArrayList;

/**
 *
 * @author faresmehanna
 */
public class Simulator implements ISimulate{

    IAgent player1, player2;
    IAgent last_player;
    IRiskGame game;

    public Simulator() {
        game = null;
    }

    private IAgent selectAgent(String agentName) {
        switch(agentName) {
            case "AStar":
                return new AStarAgent();
            case "Aggressive":
                return new AggressiveAgent();
            case "Greedy":
                return new GreedyAgent();
            case "NearlyPacifist":
                return new NearlyPacifistAgent();
            case "Passive":
                return new PassiveAgent();
            case "Huname":
                return null;
        }
        return null;
    }

    @Override
    public void SelectFirstAgent(String player) {
        player1 = selectAgent(player);
        if(game != null && player1 != null) {
            ArrayList<Integer> players = game.get_players_ids();
            player1.set_game_info((RiskGame) game, players.get(0), players.get(1));
        }
    }

    @Override
    public void SelectSecondAgent(String player) {
        player2 = selectAgent(player);
        if(game != null && player2 != null) {
            ArrayList<Integer> players = game.get_players_ids();
            player2.set_game_info((RiskGame) game, players.get(1), players.get(0));
        }
    }

    @Override
    public void SetGameObject(IRiskGame game) {
        this.game = game;
        ArrayList<Integer> players = game.get_players_ids();
        if(player1 != null) {
            player1.set_game_info((RiskGame) game, players.get(0), players.get(1));
        }
        if(player2 != null) {
            player2.set_game_info((RiskGame) game, players.get(1), players.get(0));
        }
    }

    @Override
    public int Simulate() {
        while(true) {
            //make player1 move
            player1.make_move();
            if(game.is_game_end()) {
                return game.get_winning_player();
            }
            //make player2 move
            player2.make_move();
            if(game.is_game_end()) {
                return game.get_winning_player();
            }
        }
    }

    @Override
    public Boolean SimulateSingleStep2Agents() {

        //if game ends
        if(game.is_game_end()) {
            return false;
        }

        //if this is the first step
        if(last_player == null) {
            player1.make_move();
            last_player = player1;
            return true;
        }

        //handle two agents
        if(last_player == player1) {
            player2.make_move();
            last_player = player2;
            return true;
        }
        else if(last_player == player2) {
            player1.make_move();
            last_player = player1;
            return true;
        }

        return false;
    }

    @Override
    public Boolean SimulateSingleStep1Agent() {

        //if game ends
        if(game.is_game_end()) {
            return false;
        }

        //handle human agent
        if(player1 == null) {
            player2.make_move();
            return true;
        }
        if(player2 == null) {
            player1.make_move();
            return true;
        }

        return false;
    }

}