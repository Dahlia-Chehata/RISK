package Model;

import Interfaces.IAgent;
import Simulator.Simulator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import javafx.util.Pair;

class AStarGameScore implements Comparable<AStarGameScore> {

    public final AStarGameScore parent_;
    private final RiskGame game_;
    private final int player_id_;
    private final int enemy_id_;
    public int h_val_;
    public int state_;
    public int cost_;
    public String action_;


    AStarGameScore(RiskGame game, AStarGameScore parent, int state, int cost, int player_id, int enemy_id, String action) {
        player_id_ = player_id;
        enemy_id_ = enemy_id;
        h_val_ = cost+heuristic(game);
        parent_ = parent;
        action_ = action;
        state_ = state;
        game_ = game;
        cost_ = cost;
    }

    RiskGame get_game_obj() {
        return game_;
    }

    private int heuristic(RiskGame game) {
        return game.get_player_countries(enemy_id_).size()*game.get_player_countries(enemy_id_).size();
    }

    public static Comparator<AStarGameScore> idComparator = new Comparator<AStarGameScore>(){
        @Override
        public int compare(AStarGameScore c1, AStarGameScore c2) {
            return (int) (c1.h_val_ - c2.h_val_);
        }
    };

    @Override
    public int compareTo(AStarGameScore o) {
        return (int) (this.h_val_ - o.h_val_);
    }

};



/**
 *
 * @author faresmehanna
 */
public class AStarAgent implements IAgent{

    private RiskGame game_;
    private int my_id_;
    private int enemy_id_;
    private Boolean random_moves_;

    private List<AStarGameScore> wining_moves_;
    private int wining_moves_ptr_ = 0;

    public AStarAgent() {
        wining_moves_ = new LinkedList<>();
        random_moves_ = false;
        enemy_id_ = -1;
        game_ = null;
        my_id_ = -1;
    }

    @Override
    public void set_game_info(RiskGame game, int player_id, int enemy_id) {
        game_ = game;
        my_id_ = player_id;
        enemy_id_ = enemy_id;
    }

    @Override
    public void make_move() {

        //handle first call
        if(random_moves_ == false && wining_moves_.size() == 0) {
            try {
                A_Star_initialization();
            } catch (CloneNotSupportedException ex) {
                random_moves_ = true;
            }
        }

        if(random_moves_) {
            //move the army to any country I have
            game_.set_cp_soldiers(game_.get_player_countries(my_id_).get(0));
            //do any attack if possible
            if(game_.get_attackable_countries(my_id_).size() > 0) {
                int my_country = game_.get_attackable_countries(my_id_).get(0).getKey();
                int enemy_country = game_.get_attackable_countries(my_id_).get(0).getValue();
                game_.cp_attack(my_country,
                        enemy_country,
                        game_.get_country_soldiers(my_country) - game_.get_country_soldiers(enemy_country) - 1,
                        1);
            }
        } else {

            /* get the moves from the list of optimal moves */

            //get the set soldiers move
            int country_id = Integer.parseInt(wining_moves_.get(wining_moves_ptr_).action_);
            game_.set_cp_soldiers(country_id);
            wining_moves_ptr_++;

            //check if there is an attack step
            String[] attack_parameters = wining_moves_.get(wining_moves_ptr_).action_.split(" ");
            if(attack_parameters.length == 4) {
                game_.cp_attack(Integer.parseInt(attack_parameters[0]),
                        Integer.parseInt(attack_parameters[1]),
                        Integer.parseInt(attack_parameters[2]),
                        Integer.parseInt(attack_parameters[3]));
                wining_moves_ptr_++;
            }
        }
        game_.end_turn();
    }

    private void A_Star_initialization() throws CloneNotSupportedException {

        //the winning state after A* runs
        AStarGameScore wining_state = null;

        //priority queue to hold current states of the game
        Queue<AStarGameScore> game_states_pq = new PriorityQueue<>();

        //add the first game
        RiskGame first_game = (RiskGame) game_.clone();
        game_states_pq.add(new AStarGameScore(first_game, null, 0, 0, my_id_, enemy_id_,"NOACTION"));

        //visited set
        HashSet<RiskGame> visited = new HashSet();

        while(!game_states_pq.isEmpty()) {

            //get the best possible game state
            AStarGameScore best_game = game_states_pq.poll();
            RiskGame curr_game = best_game.get_game_obj();

            //check if it's a wining state
            if(curr_game.is_game_end() && curr_game.get_winning_player() == my_id_) {
                wining_state = best_game;
                break;
            }

            //check if it's a lose state
            if(curr_game.is_game_end() && curr_game.get_winning_player() == enemy_id_) {
                continue;
            }

            if(visited.contains(curr_game)) {
                continue;
            }
            visited.add(curr_game);

            //check who is the current player
            if(curr_game.get_current_player_id() == my_id_) {
                //if it is my agent, then check what move it is
                //if state == 0, then move the army
                //if state == 1, then do the attack
                if (best_game.state_ == 0) {
                    //get my countries
                    ArrayList<Integer> my_countries = curr_game.get_player_countries(my_id_);
                    //for each country try to move the army to this country
                    for(int i=0; i<my_countries.size(); i++) {
                        //move the army only and get ready for attack
                        RiskGame tried_game1 = (RiskGame) curr_game.clone();
                        tried_game1.set_cp_soldiers(my_countries.get(i));
                        if(!visited.contains(tried_game1))
                            game_states_pq.add(new AStarGameScore(tried_game1, best_game, 1, best_game.cost_+1, my_id_, enemy_id_, String.valueOf(my_countries.get(i))));
                        //move the army and skip the attack
                        RiskGame tried_game2 = (RiskGame) curr_game.clone();
                        tried_game2.set_cp_soldiers(my_countries.get(i));
                        tried_game2.end_turn();
                        if(!visited.contains(tried_game2))
                            game_states_pq.add(new AStarGameScore(tried_game2, best_game, -1, best_game.cost_+1, my_id_, enemy_id_, String.valueOf(my_countries.get(i))));
                    }

                } else {
                    //try all possible attacks
                    ArrayList<Pair<Integer, Integer>> attack_pairs = curr_game.get_attackable_countries(my_id_);
                    for(int j=0; j<attack_pairs.size(); j++) {
                        int my_country_id = attack_pairs.get(j).getKey();
                        int enemy_country_id = attack_pairs.get(j).getValue();
                        //try every possible combinations of scores between the countries
                        int score = curr_game.get_country_soldiers(my_country_id) - curr_game.get_country_soldiers(enemy_country_id);
                        for(int k=1; k<score; k++) {
                            //do the attack
                            RiskGame tried_game = (RiskGame) curr_game.clone();
                            tried_game.cp_attack(my_country_id, enemy_country_id, k, score-k);
                            tried_game.end_turn();
                            String action = String.valueOf(my_country_id) + " " + String.valueOf(enemy_country_id) + " " + String.valueOf(k) + " " + String.valueOf(score-k);
                            //push to the queue
                            if(!visited.contains(tried_game))
                                game_states_pq.add(new AStarGameScore(tried_game, best_game, -1, best_game.cost_, my_id_, enemy_id_, action));
                        }
                    }
                }
            } else {
                //if it is the enemy then play as a passive agent
                RiskGame tried_game = (RiskGame) curr_game.clone();
                IAgent enemy_agent = new PassiveAgent();
                enemy_agent.set_game_info(tried_game, enemy_id_, my_id_);
                enemy_agent.make_move();
                //add it back to the queue
                if(!visited.contains(tried_game))
                    game_states_pq.add(new AStarGameScore(tried_game, best_game, 0, best_game.cost_+1, my_id_, enemy_id_, "ENEMYACTION"));
            }
        }

        //all moves will lose
        if(wining_state == null) {
            //do any placement and any move
            random_moves_ = true;
            return;
        }


        //if there is a winning state, then conver it to an arrayList
        AStarGameScore curr_state = wining_state;
        while(wining_state.cost_ != 0) {
            if(!wining_state.action_.equals("ENEMYACTION")) {
                wining_moves_.add(0, wining_state);
            }
            wining_state = wining_state.parent_;
        }
    }

    private int heuristic_ev(RiskGame game) {
        return game.get_player_countries(enemy_id_).size();
    }



}