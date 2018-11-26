package Interfaces;

public interface ISimulate {
    /*
    * this class act as a factory for the 2 agents
    * */
    public void selectFirstAgent(String player);
    public void selectSecondAgent(String player);
    /*
    * It returns the number of the winner player
    * */
    public void firstMakeStep();
    public void secondMakeStep();

}
