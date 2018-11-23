package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
		Collections.sort(defenderList,Country.ArmyComparator); //defender country with largest army
		Country attacker = strongestCountry,defender=defenderList.get(defenderList.size()-1);	

		while (defender.getCurrentArmiesNumber() > 0 && attacker.getCurrentArmiesNumber() >= 1) {
				int diceAttacker = AttackPhase.getRandomDice(attacker, 3);
				int diceDefender = AttackPhase.getRandomDice(attacker, 2);
				AttackPhase.startBattle(attacker, defender, diceAttacker, diceDefender);
			}

			if (defender.getCurrentArmiesNumber() == 0) {
				System.out.println("Aggressive Player captured country" + defender.getcountryNumber());
				updateCountryOwner(defender, attacker);
				getAdditionalBonus();
				setWinner(true);
			}
			if (attacker.getCurrentArmiesNumber() < 1) {
				System.out.println("Aggressive Player lost the battle to country " + defender.getcountryNumber());
			}

//			if (AttackPhase.isEndOfAttack(this)) {
//				System.out.println("End of Attack");
//				break;
//			}
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
	@Override
	public void reinforce(Country country) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void attack(Country attacker, Country defender) {
		// TODO Auto-generated method stub
		
	}
}
