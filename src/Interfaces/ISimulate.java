package Interfaces;

public interface ISimulate {
    
    /*
    * this class act as a factory for the 2 agents
    * */
    public void SelectFirstAgent(String player);
    public void SelectSecondAgent(String player);
    public void SetGameObject(IRiskGame game);

    /*
    * It returns the number of the winner player
    * */
    public int Simulate();
    
    /*
    * SimulateSingleStep between 2 AI agents.
    * */
    public Boolean SimulateSingleStep2Agents();
    
    /*
    * SimulateSingleStep between 1 AI agent and human agent.
    * */
    public Boolean SimulateSingleStep1Agent();
	public IAgent getPlayer1();
    
}
