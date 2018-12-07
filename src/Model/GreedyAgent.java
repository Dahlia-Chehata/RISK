package Model;

import Interfaces.IAgent;
import java.util.ArrayList;

import javafx.util.Pair;

/**
 *
 * @author faresmehanna
 */
public class GreedyAgent implements IAgent{

    RiskGame game_;
    int my_id_;
    int enemy_id_;
    public int expansions,turns;
    
    public GreedyAgent() {
        enemy_id_ = -1;
        game_ = null;
        my_id_ = -1;
        expansions=0;
        turns=0;
    }
    
    @Override
    public void set_game_info(RiskGame game, int player_id, int enemy_id) {
        game_ = game;
        my_id_ = player_id;
        enemy_id_ = enemy_id;
    }

    @Override
    public void make_move() {
        try {
            greedy_step();
        } catch (CloneNotSupportedException ex) {
        }
        game_.end_turn();
        turns++;
    }
    
    private void greedy_step() throws CloneNotSupportedException {
        
        //get my countries
        ArrayList<Integer> my_countries = game_.get_player_countries(my_id_);
        
        //the selected moves after greedy runs
        int cp_soldiers_country = my_countries.get(0);
        Pair<Integer, Integer> selected_attack = null;
        Pair<Integer, Integer> selected_compination = null;
        int heuristic_val = Integer.MIN_VALUE;
        
        //for each country try to move the army to this country
        for(int i=0; i<my_countries.size(); i++) {
            
            RiskGame tried_game = (RiskGame) game_.clone();
            tried_game.set_cp_soldiers(my_countries.get(i));
            
            //try every attack possible after moving the bonus army
            ArrayList<Pair<Integer, Integer>> attack_pairs = tried_game.get_attackable_countries(my_id_);
            for(int j=0; j<attack_pairs.size(); j++) {
                
                int my_country_id = attack_pairs.get(j).getKey();
                int enemy_country_id = attack_pairs.get(j).getValue();
            
                //try every possible combinations of scores
                int score = tried_game.get_country_soldiers(my_country_id) - tried_game.get_country_soldiers(enemy_country_id);
                for(int k=1; k<score; k++) {
                    RiskGame tried_game2 = (RiskGame) tried_game.clone();
                    tried_game2.cp_attack(my_country_id, enemy_country_id, k, score-k);
                    
                    //evaluate those moves, if it was better than the best yet, then select it.
                    if(heuristic_ev(tried_game2) > heuristic_val) {
                    	expansions ++;
                        cp_soldiers_country = my_countries.get(i);
                        selected_attack = attack_pairs.get(j);
                        selected_compination = new Pair(k, score-k);
                        heuristic_val = heuristic_ev(tried_game2); 
                    }
                }
            }
            
        }
        
        //move new army to the selected country
        game_.set_cp_soldiers(cp_soldiers_country);
        
        //execute the best attack if possible
        if(selected_attack != null && selected_compination != null) {
            game_.cp_attack(selected_attack.getKey(), selected_attack.getValue(), selected_compination.getKey(), selected_compination.getValue());
        }
        
    }

    private int heuristic_ev(RiskGame game) {
        return game.get_player_countries(enemy_id_).size();
    }
}
