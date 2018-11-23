package utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import Model.Agent;
import Model.Country;

public class AttackPhase {
	/**
	 * checks if the attack phase ends 
	 * @param agent
	 * @return
	 */
   public static boolean isEndOfAttack(Agent agent) {
	   for (Country country : agent.getOwnedCountries()) {
			if (country.getCurrentArmiesNumber() > 1) {
				return false;
			}
		}
		return true;
   }
   /**
    * comparing dice rolls for attacker and defendant
    * @param attacker
    * @param defender
    * @param attackDiceRoll
    * @param defendDiceRoll
    */
   public static void startBattle(Country attacker, Country defender, int attackDiceRoll, int defendDiceRoll) {
	    Integer[] attackDice = new Integer[attackDiceRoll];
		Integer[] defendDice = new Integer[defendDiceRoll];
		Random random = new Random();

		for (int i = 0; i < attackDiceRoll; i++)
			attackDice[i] = random.nextInt(6) + 1;
		for (int i = 0; i < defendDiceRoll; i++)
			defendDice[i] = random.nextInt(6) + 1;

		// Sort in descending order to find their respective best dice roll
		Arrays.sort(attackDice, Collections.reverseOrder());
		Arrays.sort(defendDice, Collections.reverseOrder());
		
		// for logging purposes
		String message = "Caclulating the highest dice roll";
		System.out.println(message);
		 
		//Comparing the best dice roll for both cases : attackDicceRoll== 1 and attackDiceRoll>1
		if (attackDice[0] > defendDice[0]) {
			defender.looseArmy();
		}
		else {
			attacker.looseArmy();
		}
		
		if (attackDiceRoll >1) {
			if (defendDiceRoll == 2) {
				// Comparing 2nd best dice roll
				if (attackDice[1] > defendDice[1]) {
					defender.looseArmy();
				} else {
					attacker.looseArmy();
				}
			}
		}	
   }
}
