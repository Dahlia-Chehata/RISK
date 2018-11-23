package Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import utilities.AttackPhase;

public class HumanAgent extends Agent {
	private List<Country> defenderCountries;

	public HumanAgent() {
		super();
		setAI(false);
		defenderCountries = new ArrayList<>();
	}

	@Override
	public void reinforce(Country country) {
		setArmiesNumber(getArmiesNumber() + reinforcementArmies());
		country.setCurrentArmiesNumber(country.getCurrentArmiesNumber() + getArmiesNumber());
		setArmiesNumber(0);
	}

	@Override
	public void attack(Country attacker, Country defender) {
		
	    if (!getOwnedCountries().contains(attacker)) {
	    	System.out.println("Choose a counntry from your own list");
	    	return;
	    }
		List<Country> defenderList = getDefenders(attacker);
		if (!defenderList.contains(defender)) {
			System.out.println("You cannot attack a non neighbour country ");
	    	return;
		}
		while (defender.getCurrentArmiesNumber() > 0 && attacker.getCurrentArmiesNumber() >= 1) {
			int diceAttacker = AttackPhase.getRandomDice(attacker, 3);
			int diceDefender = AttackPhase.getRandomDice(attacker, 2);
			AttackPhase.startBattle(attacker, defender, diceAttacker, diceDefender);
		}
		
		if (attacker.getCurrentArmiesNumber() < 1) {
			System.out.println("Player lost the battle to country " + defender.getcountryNumber());
			return;
		}else if (defender.getCurrentArmiesNumber() == 0) {
				defender.setCurrentArmiesNumber(0);
			    updateCountryOwner(defender, attacker);
				setWinner(true);
				getAdditionalBonus();
				System.out.println("Player captured country" + defender.getcountryNumber());
		}
	}

	private List<Country> getDefenders(Country country) {

		LinkedList<Country> neighbours = RiskGame.getNeighbors().get(country);
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
	public void reinforce() {
		// TODO Auto-generated method stub

	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub

	}
}
