package Model2;

import Interfaces.IAgent;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author faresmehanna
 */
public class NearlyPacifistAgent implements IAgent{

    RiskGame game_;
    int my_id_;
    int enemy_id_;
    
    public NearlyPacifistAgent() {
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
        place_armies();
        attack_if_possible();
        game_.end_turn();
    }
    
    private void place_armies() {
        
        int choosen_country_id = -1;
        int army_in_country = Integer.MIN_VALUE;
        
        //get my countries
        ArrayList<Integer> my_countries = game_.get_player_countries(my_id_);
        
        //get the strongest country
        for(int i=0; i<my_countries.size(); i++) {
            if(game_.get_country_soldiers(my_countries.get(i)) > army_in_country) {
                army_in_country = game_.get_country_soldiers(my_countries.get(i));
                choosen_country_id = my_countries.get(i);
            }
        }
        
        //move new army to this country
        game_.set_cp_soldiers(choosen_country_id);
        
    }
    
    private void attack_if_possible() {
        
        //get attackable moves
        ArrayList<Pair<Integer, Integer>> attackable_pairs = game_.get_attackable_countries(my_id_);
        
        //no attack possible
        if(attackable_pairs.isEmpty()) {
            return;
        }
        
        //pick the attack with the least amount of enemy soldiers
        Pair<Integer, Integer> best_attack_pair = null;
        int least_enemy_soldiers = Integer.MAX_VALUE;    //attack score is the total number of army left after the attack
        int best_attack_score = 0;    //attack score is the total number of army left after the attack
        
        for(int i=0; i<attackable_pairs.size(); i++) {
            
            int my_country_id = attackable_pairs.get(i).getKey();
            int enemy_country_id = attackable_pairs.get(i).getValue();
            
            if(game_.get_country_soldiers(enemy_country_id) < least_enemy_soldiers) {
                least_enemy_soldiers = game_.get_country_soldiers(enemy_country_id);
                best_attack_score = game_.get_country_soldiers(my_country_id) - game_.get_country_soldiers(enemy_country_id);
                best_attack_pair = attackable_pairs.get(i);
            }
        }
        
        //execute the best attack possible
        game_.cp_attack(best_attack_pair.getKey(), best_attack_pair.getValue(), best_attack_score - 1, 1);
        
    }
}
