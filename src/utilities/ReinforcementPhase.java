package utilities;

import Model.Agent;
import Model.Country;

public class ReinforcementPhase {
	/**
	 * defines the placement of the army for a player in each turn except for the first turn 
	 * @param agent
	 * @return number of reinforcement armies
	 */
	public static int calculateReinforcementArmies(Agent agent) {
		int ownedCountries = agent.getNumberOfOwnedCountries();
		int reinforcementArmies = (int) Math.floor(ownedCountries / 3);
		// Minimum number of armies for any player in case reinforcement armies are less than 3
		if (reinforcementArmies < 3) {
			reinforcementArmies = 3;
		}
		return reinforcementArmies;
	}
	/**
	 * check if agent has occupied a whole continent
	 * @param agent
	 * @return true if acquired whole continent
	 */
	
	public static boolean hasWholeContinent(Agent agent) {
		Country country1 = agent.getOwnedCountries().get(0);
		for (Country country : agent.getOwnedCountries()) {
			if (country.getContinentNumber()!= country1.getContinentNumber()) {
				return false;
			}
		}
		return true;
	}
}
