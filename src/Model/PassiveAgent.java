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
	public void attack() {
		// Nothing to do here

	}

}
