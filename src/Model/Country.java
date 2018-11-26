package Model;

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
}
