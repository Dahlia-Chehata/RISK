package Interfaces;

import java.util.ArrayList;

import Model.Agent;
import javafx.util.Pair;

public interface IRiskGame {
    
    
    /*******************************************************
     * methods to set the initial state of the game.
     * these functions must be called before the starting of the game.
     * if the operation done successfully a true will return, false otherwise.
     */
    Boolean set_count_of_countries(int countries_count);
    Boolean add_edge(int country_id_1, int country_id_2);
    Boolean set_count_of_paritions(int partitions_count);
    Boolean add_partition(int partition_bounce, ArrayList<Integer> partition_countries_ids);
    Boolean add_soldiers(int player_id, int country_id, int soldiers_count);
    

    /*******************************************************
     * declaring the start of a game.
     * after setting the initial state. you must start the game to start playing.
     * if not enough data to start the game, the function will return falsel.
     */
    Boolean start_game();
    

   
    /*******************************************************
     * The following methods are supported during the game and can be called by
     * anyone anytime to know the current state  of the game.
     */
    
    /**
     * get the players ids in this game.
     * this will return 0,1 for this two players game.
     */
    ArrayList<Integer> get_players_ids();
    
    /**
     * get the current player id in this turn.
     */
    int get_current_player_id();

    /**
     * get the current player's countries.
     */
    ArrayList<Integer> get_player_countries(int player_id);
    
    /**
     * get the current player's continents if any.
     */
    ArrayList<Integer> get_player_continents(int player_id);
    
    /**
     * get the country soldiers.
     */
    int get_country_soldiers(int country_id);
    
    /**
     * get the player_id owner of the country.
     */
    int get_country_owner(int country_id);
    
    /**
     * get the countries_id of this country.
     */
    ArrayList<Integer> get_country_neighbours(int country_id);
    
    /**
     * return list of countries that player can attack.
     * return pair, the first country is a country owned by the player.
     * and the second country is the country owned by the enemy.
     */
    ArrayList<Pair<Integer,Integer>> get_attackable_countries(int player_id);
    
    /**
     * return True if the game ended, false otherwise.
     */
    Boolean is_game_end();
    
    /**
     * return the id of the winner player if the game ended, -1 otherwise.
     */
    int get_winning_player();
    
    /**
     * return new object of the game with the same current state.
     */
    IRiskGame clone_game();
    
    
    
    /*******************************************************
     * The following methods are for agents to make a change in the state of the
     * game, these methods should only be called inside the agent code and must
     * be called in correct order "beltarteeb xD".
     */
    
    int get_cp_reinforcement_soldiers();
    int get_cp_bounce_soldiers();
    
    /**
     * this is the first step in the game and it will put all reinforcement + bounce
     * soldiers in the selected country. this country must be owned by the 
     * current player.
     * @return true if country owned by the current player, false otherwise.
     */
    Boolean set_cp_soldiers(int country_id);
    
    /**
     * this is the second step in the game and it will attack the enemy 
     * selected country. this country must be from get_attackable_countries.
     * you must also specify the number of soldiers in old country and number of
     * soldiers in the new country after the attack.
     * num_old_country + num_new_country = the number of the soldiers left after
     * the attack.
     * @return true if attack done successfully, false otherwise.
     */
    Boolean cp_attack(int own_country_id, int enemy_country_id, int num_old_country, int num_new_country);
    
    
    /**
     * this end the current turn for the current player.
     * this function is optional if the player done single attack on the enemy.
     * and it's required to be called if the player didn't attack the enemy.
     * @return true if no reinforcement or bounce soldiers remaining, false
     * otherwise.
     */
    Boolean end_turn();
}