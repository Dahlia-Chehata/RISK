package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Dahlia
 *
 */
// continent stands for partition
public class Continent {

	private int continentNumber;
	private List<Country> countries;
	int bonusArmies;

	public Continent(int continentNumber, int bonusArmies) {
		this.continentNumber = continentNumber;
		countries = new ArrayList<Country>();
		this.bonusArmies=bonusArmies;
	}

	public int getContinentNumber() {
		return continentNumber;
	}

	public void setContinentName(int continentNumber) {
		this.continentNumber = continentNumber;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public void addCountry(Country country) {
		countries.add(country);
	}

	public void removeCountry(Country country) {
		countries.remove(country);
	}
	public int getbonusArmies() {
		return bonusArmies;
	}
	public void setbonusArmies(int bonusArmies) {
		this.bonusArmies=bonusArmies;
	}
	public String Info() {
		return "Partition [partition number = "+ continentNumber +", countries = "+ countries
				+" bonus Armies = "+ bonusArmies +"]";
	}
}
