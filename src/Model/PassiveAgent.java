package Model;

import java.util.Collections;
import java.util.List;


public class PassiveAgent extends Agent {

	public PassiveAgent() {
		super();
		setAI(false);
	}

	@Override
	public void reinforce() {
		List<Country> countriesOwned = getOwnedCountries();
		Collections.sort(countriesOwned, Country.ArmyComparator);
		setArmiesNumber(getArmiesNumber() + reinforcementArmies());
		Country weakestCountry = countriesOwned.get(0);
		weakestCountry.setCurrentArmiesNumber(weakestCountry.getCurrentArmiesNumber() + getArmiesNumber());
		setArmiesNumber(0);
	}

	@Override
	public void diceBaseAttack() {
		// Nothing to do here

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
		List<Country> countriesOwned = getOwnedCountries();
		Collections.sort(countriesOwned, Country.ArmyComparator);
		Country weakestCountry = countriesOwned.get(0);
		weakestCountry.setCurrentArmiesNumber(getArmiesNumber()); 
		setArmiesNumber(0);		
	}

	@Override
	public void deterministicPlay(Country attacker, Country defender) {
		// TODO Auto-generated method stub
		
	}

}
