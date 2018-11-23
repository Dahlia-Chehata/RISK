package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import utilities.DeterministicGame.DeterministicAttackPhase;
import utilities.DiceBasedGame.DiceBasedAttackPhase;

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
	public void diceBaseAttack() {
		List<Country> defenderList = getDefenders();
		Collections.sort(defenderList,Country.ArmyComparator); //defender country with smallest army
		Country attacker = weakestCountry,defender=defenderList.get(0);
		int attackerInitArmiesNumber = attacker.getCurrentArmiesNumber();
		int defenderInitArmiesNumber = defender.getCurrentArmiesNumber();
		
		while (defender.getCurrentArmiesNumber() > 0 && attacker.getCurrentArmiesNumber() > 1) {
			int diceAttacker = DiceBasedAttackPhase.getRandomDice(attacker, 3);
			int diceDefender = DiceBasedAttackPhase.getRandomDice(attacker, 2);
			DiceBasedAttackPhase.startBattle(attacker, defender, diceAttacker, diceDefender);
		}
		if (attacker.getCurrentArmiesNumber() == 1) {
			System.out.println("Cannot Attack: Lacking Armies");
			// return to the initial state
			attacker.setCurrentArmiesNumber(attackerInitArmiesNumber);
			defender.setCurrentArmiesNumber(defenderInitArmiesNumber);
		}else if (defender.getCurrentArmiesNumber() == 0) {
				defender.setCurrentArmiesNumber(0);
				DiceBasedAttackPhase.updateCountryOwner(defender, attacker);
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

	@Override
	public void reinforce(Country country) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void diceBasedAttack(Country attacker, Country defender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deterministicPlay() {
		List<Country> defenderList = getDefenders();
		Collections.sort(defenderList,Country.ArmyComparator); //defender country with smallest army
		Country attacker = weakestCountry,defender=defenderList.get(0);
		
		// reinforcement 
		List<Country> countriesOwned = getOwnedCountries();
		Collections.sort(countriesOwned, Country.ArmyComparator);
		Country weakestCountry = countriesOwned.get(0);
		weakestCountry.setCurrentArmiesNumber(getArmiesNumber()); // current armies + 2 additional bonus in strongest country 
		setArmiesNumber(0);		
		
		//Attack
		DeterministicAttackPhase.startBattle(attacker, defender);
		
		if (defender.getCurrentArmiesNumber() == 0) {
			System.out.println("Pacifist Player captured country " + defender.getcountryNumber());
			DeterministicAttackPhase.AfterAttack(attacker, defender);
			getAdditionalBonus();
			setWinner(true);
		}		
	}

	@Override
	public void deterministicPlay(Country attacker, Country defender) {
		// TODO Auto-generated method stub
		
	}
}
