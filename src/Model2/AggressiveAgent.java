package Model2;

import Interfaces.IAgent;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author faresmehanna
 */
public class AggressiveAgent implements IAgent{

    RiskGame game_;
    int my_id_;
    int enemy_id_;
    
    public AggressiveAgent() {
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
        
        //if the user have continent/s then try to attack any country to lose a continent, pick biggest first.
        if(!attack_against_biggest_continent()) {
            //if can't do such attack, then attack the country with the most soldiers.
            attack_with_most_enemy_soldiers();
        }
        
    }
    
    private void attack_with_most_enemy_soldiers() {
        
        //get attackable moves
        ArrayList<Pair<Integer, Integer>> attackable_pairs = game_.get_attackable_countries(my_id_);
        
        //no attack possible
        if(attackable_pairs.isEmpty()) {
            return;
        }
        
        //pick the attack with the most amount of enemy soldiers
        Pair<Integer, Integer> best_attack_pair = null;
        int most_enemy_soldiers = Integer.MIN_VALUE;    //attack score is the total number of army left after the attack
        int best_attack_score = 0;    //attack score is the total number of army left after the attack
        
        for(int i=0; i<attackable_pairs.size(); i++) {
            
            int my_country_id = attackable_pairs.get(i).getKey();
            int enemy_country_id = attackable_pairs.get(i).getValue();
            
            if(game_.get_country_soldiers(enemy_country_id) > most_enemy_soldiers) {
                most_enemy_soldiers = game_.get_country_soldiers(enemy_country_id);
                best_attack_score = game_.get_country_soldiers(my_country_id) - game_.get_country_soldiers(enemy_country_id);
                best_attack_pair = attackable_pairs.get(i);
            }
        }
        
        //execute the best attack possible
        game_.cp_attack(best_attack_pair.getKey(), best_attack_pair.getValue(), best_attack_score - 1, 1);
    }
    
    private Boolean attack_against_biggest_continent() {
        
        //get enemy continents
        ArrayList<Integer> enemy_continents = game_.get_player_continents(enemy_id_);
        //get attacking moves
        ArrayList<Pair<Integer, Integer>> attackable_pairs = game_.get_attackable_countries(my_id_);
        //possible attacks
        ArrayList<Pair<Integer, Integer>> possible_attacks = new ArrayList();
        ArrayList<Integer> possible_attacks_continents = new ArrayList();
        
        if(enemy_continents.isEmpty() || attackable_pairs.isEmpty()) {
            return false;
        }
        
        //extract all possible attack on any continent
        for(int i=0; i<enemy_continents.size(); i++) {
            //get all the countries in this continent
            ArrayList<Integer> continent_countries = game_.get_continent_countries(enemy_continents.get(i));
            //mark all existed possible attack
            for(int j=0; j<continent_countries.size(); j++) {
                for(int k=0; k<attackable_pairs.size(); k++) {
                    int enemy_country_id = possible_attacks.get(i).getValue();
                    if(enemy_country_id == continent_countries.get(j)) {
                        possible_attacks.add(attackable_pairs.get(k));
                        possible_attacks_continents.add(continent_countries.get(j));
                    }
                    
                }
            }
        }
        
        //if not found any, return false.
        if(possible_attacks.isEmpty()) {
            return false;
        }   
        
        Pair<Integer, Integer> best_attack_pair = null;
        int best_attack_continent = Integer.MIN_VALUE;    //to select the best continent
        int best_attack_country = Integer.MIN_VALUE;    //to select the best country in the best continent
        int best_attack_score = 0;    //attack score is the total number of army left after the attack
        
        
        //pick the best
        for(int i=0; i<possible_attacks.size(); i++) {
            
            int enemy_continent = possible_attacks_continents.get(i);
            int enemy_country_id = possible_attacks.get(i).getValue();
            int my_country_id = possible_attacks.get(i).getKey();
            
            
            //if this is new continent with more bonus value, then pick it first
            //without checking the enemy soldiers
            if(game_.get_continent_bonus(enemy_continent) > best_attack_continent) {
                best_attack_score = game_.get_country_soldiers(my_country_id) - game_.get_country_soldiers(enemy_country_id);
                best_attack_continent = game_.get_continent_bonus(enemy_continent);
                best_attack_country = game_.get_country_soldiers(enemy_country_id);
            } 
            
            //if this continent bonus is the same as the one we have then compare
            // by number of soldiers.
            else if (game_.get_continent_bonus(enemy_continent) == best_attack_continent) {
                if(game_.get_country_soldiers(enemy_country_id) > best_attack_country) {
                    best_attack_score = game_.get_country_soldiers(my_country_id) - game_.get_country_soldiers(enemy_country_id);
                    best_attack_continent = game_.get_continent_bonus(enemy_continent);
                    best_attack_country = game_.get_country_soldiers(enemy_country_id);
                }
            }
        }
        
        //execute the best attack possible
        game_.cp_attack(best_attack_pair.getKey(), best_attack_pair.getValue(), best_attack_score - 1, 1);
        
        return true;
    }
    
}
