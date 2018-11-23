package Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import utilities.AttackPhase;

public class AggressiveAgent extends Agent {
    
	private List<Country> defenderCountries;
	private Country strongestCountry;
	public AggressiveAgent() {
		super();
		setAI(false);
		defenderCountries= new ArrayList<>();
	    strongestCountry= getStrongestCountry();	
	}
	@Override
	public void reinforce() {
	   setArmiesNumber(getArmiesNumber() + reinforcementArmies());
       strongestCountry.setCurrentArmiesNumber(strongestCountry.getCurrentArmiesNumber()+getArmiesNumber());
       setArmiesNumber(0);
	}
	

	@Override
	public void attack() {
		List<Country> defenderList = getDefenders();
		Country attacker = strongestCountry;	
		for (Country defender : defenderList) {
			while (defender.getCurrentArmiesNumber() > 0 && attacker.getCurrentArmiesNumber() > 1) {
				int diceAttacker = getRandomDice(attacker, 3);
				int diceDefender = getRandomDice(attacker, 2);
				AttackPhase.startBattle(attacker, defender, diceAttacker, diceDefender);
			}

			if (defender.getCurrentArmiesNumber() == 0) {
				System.out.println("Aggressive Player captured country" + defender.getcountryNumber());
				updateCountryOwner(defender, attacker);
				setWinner(true);
			}
			if (attacker.getCurrentArmiesNumber() == 1) {
				System.out.println("Aggressive Player lost the battle to country " + defender.getcountryNumber());
				break;
			}

			if (AttackPhase.isEndOfAttack(this)) {
				System.out.println("End of Attack");
				break;
			}
		}

	}
	/**
	 * get the defenders countries around the country with the largest armies number
	 * @return
	 */
	
	private List<Country> getDefenders() {
		
		LinkedList<Country> neighbours = RiskGame.getNeighbors().get(strongestCountry);
		
		for (int i = 0; i < neighbours.size(); i++) {
			Country adjacentCountry = neighbours.get(i);
			if (!adjacentCountry.getPlayerName().equals(getPlayerName())) {
				defenderCountries.add(adjacentCountry);
			}
		}
		return defenderCountries;
	}
	private int getRandomDice(Country attacker, int maxDice) {
		int attackerArmies = attacker.getCurrentArmiesNumber();
		Random random;
		int diceCount;
		if (attackerArmies <= maxDice) {
			random = new Random();
			diceCount = 1 + random.nextInt(attackerArmies - 1);
		} else {
			random = new Random();
			diceCount = 1 + random.nextInt(maxDice);
		}
		return diceCount;
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
	private Country getStrongestCountry() {
		Country strongestCountry = getOwnedCountries().get(0);
		for (Country country :getOwnedCountries()) {
			if ((country.getCurrentArmiesNumber() > strongestCountry.getCurrentArmiesNumber())||
					(country.getCurrentArmiesNumber() == strongestCountry.getCurrentArmiesNumber()&&country.getcountryNumber()<strongestCountry.getcountryNumber())) {
				strongestCountry = country;
			}
		}
		return strongestCountry;
	}
}
