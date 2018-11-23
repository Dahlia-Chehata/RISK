package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import utilities.DeterministicGame.DeterministicAttackPhase;
import utilities.DiceBasedGame.DiceBasedAttackPhase;

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
	public void diceBaseAttack() {
		List<Country> defenderList = getDefenders();
		Collections.sort(defenderList,Country.ArmyComparator); //defender country with largest army
		Country attacker = strongestCountry,defender=defenderList.get(defenderList.size()-1);	

		while (defender.getCurrentArmiesNumber() > 0 && attacker.getCurrentArmiesNumber() > 1) {
				int diceAttacker = DiceBasedAttackPhase.getRandomDice(attacker, 3);
				int diceDefender = DiceBasedAttackPhase.getRandomDice(attacker, 2);
				DiceBasedAttackPhase.startBattle(attacker, defender, diceAttacker, diceDefender);
			}

			if (defender.getCurrentArmiesNumber() == 0) {
				System.out.println("Aggressive Player captured country" + defender.getcountryNumber());
				DiceBasedAttackPhase.updateCountryOwner(defender, attacker);
				getAdditionalBonus();
				setWinner(true);
			}
			if (attacker.getCurrentArmiesNumber() == 1) {
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
	public void deterministicPlay() {
		List<Country> defenderList = getDefenders();
		Collections.sort(defenderList,Country.ArmyComparator); //defender country with largest army
		Country attacker = strongestCountry,defender=defenderList.get(defenderList.size()-1);	
		// reinforcement 
		strongestCountry.setCurrentArmiesNumber(getArmiesNumber()); // current armies + 2 additional bonus in strongest country 
		//Attack
		DeterministicAttackPhase.startBattle(attacker, defender);
		if (defender.getCurrentArmiesNumber() == 0) {
			System.out.println("Aggressive Player captured country " + defender.getcountryNumber());
			DeterministicAttackPhase.AfterAttack(attacker, defender);
			getAdditionalBonus();
			setWinner(true);
		}
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
	public void deterministicPlay(Country attacker, Country defender) {
		// TODO Auto-generated method stub
		
	}
}
