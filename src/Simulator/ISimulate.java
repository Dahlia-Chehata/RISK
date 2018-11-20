package Simulator;

public interface ISimulate {
    /*
    * this class act as a factory for the 2 agents
    * */
    public void SelectFirstAgent();
    public void SelectSecondAgent();
    /*
    * It returns the number of the winner player
    * */
    public int Simulate();
}
