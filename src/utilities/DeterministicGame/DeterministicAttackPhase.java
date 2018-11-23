package utilities.DeterministicGame;

import java.util.List;

import Model.Agent;
import Model.Country;
import Model.RiskGame;

public class DeterministicAttackPhase {
 
	public static void startBattle(Country attacker, Country defender) {
		
		int forceDifference=attacker.getCurrentArmiesNumber() -defender.getCurrentArmiesNumber();
		if (forceDifference <=1) {
			System.out.println("Cannot attack : Lacking Armies");
			return;
		}
		defender.setCurrentArmiesNumber(0);
	    attacker.setCurrentArmiesNumber(forceDifference);
	}
	private static void updateCountryOwner(Country defender, Country attacker) {
		List<Agent> playerList = RiskGame.getPlayersList();
		for (int i = 0; i < playerList.size(); i++) {
			if (playerList.get(i).getPlayerName().equals(defender.getPlayerName())) {
				playerList.get(i).removeCountry(defender);
			} else if (playerList.get(i).getPlayerName().equals(attacker.getPlayerName())) {
				playerList.get(i).addCountry(defender);
			}
		}
		defender.setPlayerName(attacker.getPlayerName());
	}
	public static void AfterAttack(Country attacker, Country defender) {
		updateCountryOwner (attacker,defender);
		attacker.looseArmy();
		defender.gainArmy();
	}
}
