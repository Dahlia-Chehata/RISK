package Model2;

import Interfaces.IRiskGame;
import static java.lang.Integer.max;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author faresmehanna
 */
public class RiskGame implements IRiskGame{
    
    //players info
    private final int player_1_; //constant value
    private final int player_2_; //constant value
    
    //countries info
    private int countries_count_;
    private ArrayList<Integer> countries_list_owner_;
    private ArrayList<Integer> countries_list_soldiers_;
    private ArrayList<ArrayList<Integer>> countries_edges_matrix_;
    private int player_1_countries_, player_2_countries_;
    
    //partitions info
    private int partitions_count_;
    private int inner_partition_counter_;
    private ArrayList<Integer> partitions_bonus_;
    private ArrayList<ArrayList<Integer>> partitions_countries_;
    
    //game info
    private Boolean game_started_;
    private Boolean game_ended_;
    private int winner_player_;
    
    //game realtime play info
    private int turn_state_;
    private int current_player_;
    private int attack_bonus_player_id_;
    private final int attack_bonus_; //constant value
    
    
    public RiskGame() {
        attack_bonus_player_id_ = -1;
        inner_partition_counter_ = 0;
        player_1_countries_ = 0;
        player_2_countries_ = 0;
        partitions_count_ = -1;
        countries_count_ = -1;
        game_started_ = false;
        game_ended_ = false;
        current_player_ = 1;
        winner_player_ = -1;
        attack_bonus_ = 2;
        turn_state_ = 0;
        player_1_ = 1;
        player_2_ = 2;
    }
    
    @Override
    public Boolean set_count_of_countries(int countries_count) {
        
        if(countries_count <= 0 || countries_count_ != -1) {
            return false;
        }
        
        if(game_started_) {
            return false;
        }
        
        //set the country count
        countries_count_ = countries_count;
        
        //set the owners of the countries to -1
        countries_list_owner_ = new ArrayList(countries_count);
        for(int i=0; i<countries_count; i++) {
            countries_list_owner_.set(i, -1);
        }
        
        //initialize the edge matrices to all zeros
        countries_edges_matrix_ = new ArrayList(countries_count);
        for(int i=0; i<countries_count; i++) {
            countries_edges_matrix_.set(i, new ArrayList(countries_count));
        }
        
        //initialize soldiers list
        countries_list_soldiers_ = new ArrayList(countries_count);
        for(int i=0; i<countries_count; i++) {
            countries_list_soldiers_.set(i, -1);
        }
        
        return true;
    }

    @Override
    public Boolean add_edge(int country_id_1, int country_id_2) {
        
        if(country_id_1 >= countries_count_ || country_id_2 >= countries_count_) {
            return false;
        }
        
        if(game_started_) {
            return false;
        }
        
        countries_edges_matrix_.get(country_id_1).set(country_id_2, 1);
        countries_edges_matrix_.get(country_id_2).set(country_id_1, 1);
        
        return true;
    }

    @Override
    public Boolean set_count_of_paritions(int partitions_count) {
        
        if(partitions_count <= 0 || partitions_count_ != -1) {
            return false;
        
        }
        
        if(game_started_) {
            return false;
        }
        
        partitions_count_ = partitions_count;
        partitions_bonus_ = new ArrayList(partitions_count);
        partitions_countries_ = new ArrayList(partitions_count);
        
        return true;
    }

    @Override
    public Boolean add_partition(int partition_bonus, ArrayList<Integer> partition_countries_ids) {
        
        if(partition_bonus < 0 || partitions_count_ == -1 || inner_partition_counter_ >= partitions_count_) {
            return false;
        }
        
        if(game_started_) {
            return false;
        }
        
        partitions_bonus_.set(inner_partition_counter_, partition_bonus);
        partitions_countries_.set(inner_partition_counter_, partition_countries_ids);
        
        inner_partition_counter_++;
        
        return true;
    }

    @Override
    public Boolean add_soldiers(int player_id, int country_id, int soldiers_count) {
        
        //already added soldiers to this country
        if(country_id >= countries_count_ || countries_list_owner_.get(country_id) != -1) {
            return false;
        }
        
        if(soldiers_count <= 0 || (player_id != player_1_ && player_id != player_2_)) {
            return false;
        }
        
        if(game_started_) {
            return false;
        }
        
        if(player_id == player_1_) {
            player_1_countries_++;
        } else {
            player_2_countries_++;
        }
        
        countries_list_owner_.set(country_id, player_id);
        countries_list_soldiers_.set(country_id, soldiers_count);
        return true;
    }

    @Override
    public Boolean start_game() {
        
        //check already set the number of countries and partitions
        if(countries_count_ == -1 || partitions_count_ == -1) {
            return false;
        }
        
        //check all partitions provided
        if(inner_partition_counter_ != partitions_count_) {
            return false;
        }
        
        //check all countries provided
        if(player_1_countries_ + player_2_countries_ != countries_count_) {
            return false;
        }
        
        if(game_started_) {
            return false;
        }
        
        game_started_ = true;
        return false;
    }

    @Override
    public ArrayList<Integer> get_players_ids() {
        ArrayList<Integer> players = new ArrayList(2);
        players.set(0, player_1_);
        players.set(1, player_2_);
        return players;
    }

    @Override
    public int get_current_player_id() {
        if(!game_started_){
            return -1;
        }
        return current_player_;
    }

    @Override
    public ArrayList<Integer> get_player_countries(int player_id) {
        
        if(!game_started_){
            return null;
        }
        
        ArrayList<Integer> player_countries = new ArrayList();
        for(int i=0; i<countries_count_; i++) {
            if(countries_list_owner_.get(i) == player_id) {
                player_countries.add(i);
            }
        }
        
        return player_countries;
    }

    @Override
    public ArrayList<Integer> get_player_continents(int player_id) {
        
        if(!game_started_){
            return null;
        }
        
        //loop on all the partitions
        ArrayList<Integer> player_continents = new ArrayList();
        for(int i=0; i<partitions_count_; i++) {
            Boolean player_continent = true;
            //loop on all partition's countries
            for(int j=0; j<partitions_countries_.get(i).size(); j++) {
                //if the owner not player_id, then mark it as false.
                if(countries_list_owner_.get(partitions_countries_.get(i).get(j)) != player_id) {
                    player_continent = false;
                    break;
                }
            }
            //if every country owned by this user, add it's id to the array list.
            if(player_continent) {
                player_continents.add(i);
            }
        }
        
        return player_continents;
    }

    @Override
    public int get_country_soldiers(int country_id) {
        if(!game_started_) {
            return -1;
        }
        if(country_id >= countries_count_) {
            return -1;
        }
        return countries_list_soldiers_.get(country_id);
    }

    @Override
    public int get_country_owner(int country_id) {
        if(!game_started_) {
            return -1;
        }
        if(country_id >= countries_count_) {
            return -1;
        }
        return countries_list_owner_.get(country_id);
    }

    @Override
    public ArrayList<Integer> get_country_neighbours(int country_id) {
        
        if(!game_started_){
            return null;
        }

        if(country_id >= countries_count_) {
            return null;
        }
        
        ArrayList<Integer> neighbours = new ArrayList();
        for(int i=0; i<countries_count_; i++) {
            if(countries_edges_matrix_.get(country_id).get(i) == 1) {
                neighbours.add(i);
            }
        }
        
        return neighbours;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> get_attackable_countries(int player_id) {
        
        if(!game_started_){
            return null;
        }

        if(player_id != player_1_ && player_id != player_2_) {
            return null;
        }
        
        ArrayList<Pair<Integer, Integer>> attackable_countries = new ArrayList();
        for(int i=0; i<countries_count_; i++) {
            
            if(countries_list_owner_.get(i) != player_id) {
                continue;
            }
            
            ArrayList<Integer> neighbours = get_country_neighbours(i);
            for(int j=0; j<neighbours.size(); j++) {
                
                if(countries_list_owner_.get(neighbours.get(j)) == player_id) {
                    continue;
                }
                
                //my country is i, enemy is neighbours.get(j)
                if(countries_list_soldiers_.get(i) - countries_list_soldiers_.get(neighbours.get(j)) > 1) {
                    attackable_countries.add(new Pair<Integer, Integer>(i, neighbours.get(j)));
                }
            }
            
        }
        
        return attackable_countries;
    }

    @Override
    public Boolean is_game_end() {
        return game_ended_;
    }

    @Override
    public int get_winning_player() {
        if(!game_ended_) {
            return -1;
        }
        return winner_player_;
    }

    @Override
    public IRiskGame clone_game() {
        
        RiskGame new_game = new RiskGame();
        
        //set number of countries
        new_game.set_count_of_countries(countries_count_);
        
        //set edges in the graph
        for (int i=0; i<countries_count_; i++) {
            for (int j=0; j<countries_count_; j++) {
                if(countries_edges_matrix_.get(i).get(j) == 1) {
                    new_game.add_edge(i, j);
                }
            }
        }
        
        //set partitions
        new_game.set_count_of_paritions(partitions_count_);
        for(int i=0; i<partitions_count_; i++) {
            new_game.add_partition(partitions_bonus_.get(i), partitions_countries_.get(i));
        }

        //set soldiers in the game
        for(int i=0; i<countries_count_; i++) {
            new_game.add_soldiers(countries_list_owner_.get(i), i, countries_list_soldiers_.get(i));
        }
        
        new_game.start_game();
        return new_game;
    }

    @Override
    public int get_cp_reinforcement_soldiers() {
        
        if(!game_started_ || game_ended_){
            return -1;
        }
        
        if(turn_state_ != 0) {
            return 0;
        }
        
        if(get_current_player_id() == player_1_) {
            return max(3, player_1_countries_/3);
        }
        
        return max(3, player_2_countries_/3);

    }

    @Override
    public int get_cp_bonus_soldiers() {
        
        if(!game_started_ || game_ended_){
            return -1;
        }
        
        if(turn_state_ != 0) {
            return 0;
        }
        
        int bonus = 0;
        
        ArrayList<Integer> continents = get_player_continents(get_current_player_id());
        for(int i=0; i<continents.size(); i++) {
            bonus += partitions_bonus_.get(continents.get(i));
        }
        
        if(get_current_player_id() == attack_bonus_player_id_) {
            bonus += attack_bonus_;
        }
        
        return bonus;
    }

    @Override
    public Boolean set_cp_soldiers(int country_id) {

        if(!game_started_ || game_ended_){
            return false;
        }
        
        if(turn_state_ != 0) {
            return false;
        }
        
        if(country_id >= countries_count_ || get_current_player_id() != countries_list_owner_.get(country_id)) {
            return false;
        }
        
        countries_list_soldiers_.set(country_id,
                        countries_list_soldiers_.get(country_id) +
                        get_cp_reinforcement_soldiers() +
                        get_cp_bonus_soldiers());
        
        attack_bonus_player_id_ = -1;
        turn_state_ = 1;
        return true;
    }

    @Override
    public Boolean cp_attack(int own_country_id, int enemy_country_id, int num_old_country, int num_new_country) {
        
        if(!game_started_ || game_ended_){
            return false;
        }
        
        if(turn_state_ != 1 && get_cp_bonus_soldiers() + get_cp_reinforcement_soldiers() > 0) {
            return false;
        }
        
        if(own_country_id >= countries_count_ || get_current_player_id() != countries_list_owner_.get(own_country_id)) {
            return false;
        }
        
        if(enemy_country_id >= countries_count_ || get_current_player_id() == countries_list_owner_.get(enemy_country_id)) {
            return false;
        }
        
        if(countries_list_soldiers_.get(own_country_id) - countries_list_soldiers_.get(enemy_country_id) <= 1) {
            return false;
        }
        
        if(countries_list_soldiers_.get(own_country_id) - countries_list_soldiers_.get(enemy_country_id) != 
                num_old_country + num_new_country) {
            return false;
        }
        
        if(num_old_country <= 1 || num_new_country <= 1) {
            return false;
        }
        
        if(get_current_player_id() == player_1_) {
            player_1_countries_++;
            player_2_countries_--;
        } else {
            player_2_countries_++;
            player_1_countries_--;
        }
        
        countries_list_owner_.set(enemy_country_id, get_current_player_id());
        countries_list_soldiers_.set(own_country_id, num_old_country);        
        countries_list_soldiers_.set(enemy_country_id, num_new_country);        
        attack_bonus_player_id_ = get_current_player_id();
        
        //set the end of the game if the enemy player have no countries left
        if(player_1_countries_ == 0 || player_2_countries_ == 0) {
            game_ended_ = true;
            winner_player_ = get_current_player_id();
        }
        
        return true;
    }

    @Override
    public Boolean end_turn() {
        if(get_cp_bonus_soldiers() + get_cp_reinforcement_soldiers() == 0) {
            if(get_current_player_id() == player_1_) {
                current_player_ = player_2_;
            } else {
                current_player_ = player_1_;
            }
            turn_state_ = 0;
            return true;
        }
        return false;
    }

    @Override
    public int get_country_continent(int country_id) {
        
        if(!game_started_){
            return -1;
        }

        if(country_id >= countries_count_) {
            return -1;
        }
        
        for(int i=0; i<partitions_countries_.size(); i++) {
            for(int j=0; j<partitions_countries_.get(i).size() ;j++) {
                if(partitions_countries_.get(i).get(j) == country_id) {
                    return i;
                }
            }
        }
        
        return -1;
    }

    @Override
    public ArrayList<Integer> get_continent_countries(int continent_id) {
       
        if(!game_started_){
            return null;
        }

        if(continent_id >= partitions_count_) {
            return null;
        }
        
        return partitions_countries_.get(continent_id);
    }

    @Override
    public int get_continent_bonus(int continent_id) {
       
        if(!game_started_){
            return -1;
        }

        if(continent_id >= partitions_count_) {
            return -1;
        }
        
        return partitions_bonus_.get(continent_id);
    }
}