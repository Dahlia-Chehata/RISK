package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PacifistAgent extends Agent {

	private List<Country> defenderCountries;
	private Country weakestCountry;

	public PacifistAgent() {
		super();
		setAI(false);
		defenderCountries = new ArrayList<>();
	}

	@Override
	public void reinforce() {
		List<Country> countriesOwned = getOwnedCountries();
		Collections.sort(countriesOwned, Country.ArmyComparator);
		setArmiesNumber(getArmiesNumber() + reinforcementArmies());
		weakestCountry = countriesOwned.get(0);
		weakestCountry.setCurrentArmiesNumber(weakestCountry.getCurrentArmiesNumber() + getArmiesNumber());
		setArmiesNumber(0);
	}

	@Override
	public void attack() {
		List<Country> defenderList = getDefenders();
		Collections.sort(defenderList,Country.ArmyComparator); //defender country with smallest army
		Country attacker = weakestCountry,defender=defenderList.get(0);
			if (defender.getCurrentArmiesNumber() - attacker.getCurrentArmiesNumber()>= 1) {
				System.out.println("Cannot Attack: Lacking Armies");
			} else {
				defender.setCurrentArmiesNumber(0);
			    updateCountryOwner(defender, attacker);
				setWinner(true);
				getAdditionalBonus();
				System.out.println("Aggressive Player captured country" + defender.getcountryNumber());
			}
	}

	/**
	 * get the defenders countries around the country with the smallest armies
	 * number
	 * 
	 * @return
	 */

	private List<Country> getDefenders() {

		LinkedList<Country> neighbours = RiskGame.getNeighbors().get(weakestCountry);

		for (int i = 0; i < neighbours.size(); i++) {
			Country adjacentCountry = neighbours.get(i);
			if (!adjacentCountry.getPlayerName().equals(getPlayerName())) {
				defenderCountries.add(adjacentCountry);
			}
		}
		return defenderCountries;
	}

	private void updateCountryOwner(Country defender, Country attacker) {
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
}
