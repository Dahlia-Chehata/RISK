package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import utilities.AttackPhase;

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
		int attackerInitArmiesNumber = attacker.getCurrentArmiesNumber();
		int defenderInitArmiesNumber = defender.getCurrentArmiesNumber();
		
		while (defender.getCurrentArmiesNumber() > 0 && attacker.getCurrentArmiesNumber() >= 1) {
			int diceAttacker = AttackPhase.getRandomDice(attacker, 3);
			int diceDefender = AttackPhase.getRandomDice(attacker, 2);
			AttackPhase.startBattle(attacker, defender, diceAttacker, diceDefender);
		}
		if (attacker.getCurrentArmiesNumber() < 1) {
			System.out.println("Cannot Attack: Lacking Armies");
			// return to the initial state
			attacker.setCurrentArmiesNumber(attackerInitArmiesNumber);
			defender.setCurrentArmiesNumber(defenderInitArmiesNumber);
		}else if (defender.getCurrentArmiesNumber() == 0) {
				defender.setCurrentArmiesNumber(0);
			    updateCountryOwner(defender, attacker);
				setWinner(true);
				getAdditionalBonus();
				System.out.println("Pacifist Player captured country" + defender.getcountryNumber());
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

	@Override
	public void reinforce(Country country) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attack(Country attacker, Country defender) {
		// TODO Auto-generated method stub
		
	}
}
