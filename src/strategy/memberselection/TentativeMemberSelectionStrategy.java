package strategy.memberselection;

import java.util.ArrayList;

import task.Task;
import agent.Agent;

public interface TentativeMemberSelectionStrategy {

	public abstract boolean searchTentativeMembers(Agent leader, Task task);
	
	public abstract void pullExecutedSubtaskByLeader(Agent leader, Task task);
	
	public abstract boolean selectTentativeMemberEverySubtask(Agent leader, Task task, ArrayList<Agent> selectedAgents, Agent[] sortedAgents);
	
	public abstract void sendOfferMessageToTentativeMembers(Task task, Agent leader);
}
