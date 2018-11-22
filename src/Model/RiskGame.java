package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;

import Interfaces.IRiskGame;
import javafx.util.Pair;

public class RiskGame implements IRiskGame {

	@Override
	public Boolean set_count_of_countries(int countries_count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean add_edge(int country_id_1, int country_id_2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean set_count_of_paritions(int partitions_count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean add_partition(int partition_bounce, ArrayList<Integer> partition_countries_ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean add_soldiers(int player_id, int country_id, int soldiers_count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean start_game() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Integer> get_players_ids() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int get_current_player_id() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Integer> get_player_countries(int player_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Integer> get_player_continents(int player_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int get_country_soldiers(int country_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_country_owner(int country_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Integer> get_country_neighbours(int country_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<Integer, Integer>> get_attackable_countries(int player_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean is_game_end() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int get_winning_player() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IRiskGame clone_game() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int get_cp_reinforcement_soldiers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_cp_bounce_soldiers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean set_cp_soldiers(int country_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean cp_attack(int own_country_id, int enemy_country_id, int num_old_country, int num_new_country) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean end_turn() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ArrayList<Agent> getPlayersList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static HashMap<Country, LinkedList<Country>> getNeighbors() {
		// TODO Auto-generated method stub
		return null;
	}

}
