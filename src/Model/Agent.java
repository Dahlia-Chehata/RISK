package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import utilities.DiceBasedGame.DiceBasedAttackPhase;
import utilities.DiceBasedGame.FactorOf3ReinforcementPhase;

public abstract class Agent extends Observable {
	private String playerName;
	private List<Country> ownedCountries;
	private List<Continent> ownedContinents;
	private int ArmiesNumber;
	private boolean isWinner;
	private boolean AI;
	private int domination;

	public Agent() {
		ownedCountries = new ArrayList<>();
		ownedContinents = new ArrayList<>();
		isWinner = false;
	}

	public boolean isAI() {
		return AI;
	}

	public void setAI(boolean AI) {
		this.AI = AI;
	}

	public List<Continent> getOwnedContinents() {
		return ownedContinents;
	}

	public void setOwnedContinents(List<Continent> ownedContinents) {
		this.ownedContinents = ownedContinents;
	}

	public int getDominationStatus() {
		return domination;
	}

	public void setDominationStatus(int percentage) {
		domination = percentage;
		setChanged();
		notifyObservers("DOMINATION");
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public List<Country> getOwnedCountries() {
		return ownedCountries;
	}

	public void setOwnedCountries(List<Country> ownedCountries) {
		this.ownedCountries = ownedCountries;
	}

	public int getArmiesNumber() {
		return ArmiesNumber;
	}

	public void setArmiesNumber(int ArmiesNumber) {
		this.ArmiesNumber = ArmiesNumber;
	}

	public int getNumberOfOwnedCountries() {
		return ownedCountries.size();
	}

	public void addCountry(Country country) {
		this.ownedCountries.add(country);
	}

	public boolean removeCountry(Country country) {
		return ownedCountries.remove(country);
	}

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	public int reinforcementArmies() {
		return FactorOf3ReinforcementPhase.calculateReinforcementArmies(this);
	}

	public void attackOpponent(Country attacker, Country defender, int attackerDice, int defenderDice) {
		DiceBasedAttackPhase.startBattle(attacker, defender, attackerDice, defenderDice);
	}

	/**
	 * add 2 armies for acquiring a country/territory
	 */
	public void getAdditionalBonus() {
		setArmiesNumber(getArmiesNumber() + 2);
	}
	
	/***************************************  Playing   *********************************/
	
	/** Dice Base game methods **/
	// non human agents
    public abstract void reinforce();
    public abstract void diceBaseAttack();
	public void diceBasedPlay() {
      reinforce();
      diceBaseAttack();
	}
	//human agent
	public abstract void reinforce(Country country) ;
	public abstract void diceBasedAttack(Country attacker, Country defender) ;
	public void diceBasedPlay (Country countryToReinforce, Country attacker, Country defender) {
		reinforce(countryToReinforce);
		diceBasedAttack (attacker,defender);
	}
	
	/** Deterministic game method **/
	// non human agents
	 public abstract void deterministicPlay();
	// human agent
	 public abstract void deterministicPlay(Country attacker, Country defender);
	 
}
