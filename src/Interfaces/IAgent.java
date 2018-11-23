package Interfaces;

import Model2.RiskGame;

/**
 *
 * @author faresmehanna
 */
public interface IAgent {
    public void set_game_info(RiskGame game, int player_id, int enemy_id);
    public void make_move();
}
