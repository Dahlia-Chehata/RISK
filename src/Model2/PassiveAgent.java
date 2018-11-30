package Model2;

import Interfaces.IAgent;
import java.util.ArrayList;

/**
 *
 * @author faresmehanna
 */
public class PassiveAgent implements IAgent{

    RiskGame game_;
    int my_id_;
    int enemy_id_;
    
    public PassiveAgent() {
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
        
        int choosen_country_id = -1;
        int army_in_country = Integer.MAX_VALUE;
        
        //get my countries
        ArrayList<Integer> my_countries = game_.get_player_countries(my_id_);   
        //get the weakest country
        for(int i=0; i<my_countries.size(); i++) {
            if(game_.get_country_soldiers(my_countries.get(i)) < army_in_country) {
                army_in_country = game_.get_country_soldiers(my_countries.get(i));
                choosen_country_id = my_countries.get(i);
            }
        }
        
        //move new army to this country
        game_.set_cp_soldiers(choosen_country_id);
        game_.end_turn();
    }
    
}
