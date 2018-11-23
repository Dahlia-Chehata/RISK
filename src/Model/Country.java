package Model;

import java.util.Comparator;
/**
 * 
 * @author Dahlia
 *
 */
// country stands for vertex
public class Country {
	
	private int countryNumber; // vertex number
	private String playerName;
	private int currentArmiesNumber;
	private int continentNumber; // partition
	private boolean isVisited;


	public Country(int countryNumber) {
		this.countryNumber = countryNumber;
	}

	public int getcountryNumber() {
		return countryNumber;
	}

	public void setcountryNumber(int countryNumber) {
		this.countryNumber = countryNumber;
	}

	public int getCurrentArmiesNumber() {
		return currentArmiesNumber;
	}

	public void setCurrentArmiesNumber(int currentArmiesNumber) {
		this.currentArmiesNumber = currentArmiesNumber;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setVisited(boolean visited) {
		isVisited = visited;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void gainArmy() {
		currentArmiesNumber++;
	}

	public void looseArmy() {
		currentArmiesNumber--;
	}

	public int getContinentNumber() {
		return continentNumber;
	}

	public void setContinentName(int continentNumber) {
		this.continentNumber = continentNumber;
	}
	
	/** The Army comparator. */
	public static Comparator<Country> ArmyComparator = new Comparator<Country>() {

		public int compare(Country country1, Country country2) {
			if (country1.getCurrentArmiesNumber() == country2.getCurrentArmiesNumber()) {
				if (country1.getcountryNumber()==country2.getcountryNumber()) {
					return 0;
				}else if(country1.getcountryNumber()>country2.getcountryNumber()) {
					return -1;
				} else {
					return 1;
				}
			}
			else if (country1.getCurrentArmiesNumber() > country2.getCurrentArmiesNumber())
				return 1;
			else
				return -1;
		}
	};
}
