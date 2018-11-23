package utilities.DiceBasedGame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Model.Agent;
import Model.Country;
import Model.RiskGame;

public class DiceBasedAttackPhase {
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
	 * 
	 * The game of RISK is played such that two players are allowed to "attack" and "defend" in the following
	 *  ways:
	 *
	 * a) the "attacker" may roll 1, 2, or 3 dice.
	 * b) the "defender" may roll 1 or 2 dice.
	 * c) the highest rolled attack dice is compared to the highest rolled defense dice, and
	 *    1. the attacker loses if the attack dice <= defense dice,
	 *    2. the defender loses if the attack dice > defense dice. 
	 * d) The compared dice are discarded, and if each player still has dice left, repeat c) above.
	 *  
	 *  The number of armies you attack with will determine how many dice you get to roll when you square off the opponent whose territory you are defending.[12]
     *  1 army = 1 die
     *  2 armies = 2 dice
     *  3 armies = 3 dice
     *  
	 *  reference : https://web.stanford.edu/~guertin/risk.notes.html
	 * 
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
		
		String message2 = "Caclulating the  second highest dice roll";
		System.out.println(message2);
		
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
  public static int getRandomDice(Country attacker, int maxDice) {
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
  public static void updateCountryOwner(Country defender, Country attacker) {
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
}
