package strategy.memberselection;

import java.util.ArrayList;

import task.Task;
import agent.ConcreteAgent;

public interface TentativeMemberSelectionStrategy {

	public abstract boolean searchTentativeMembers(ConcreteAgent leader, Task task);
	
	public abstract void pullExecutedSubtaskByLeader(ConcreteAgent leader, Task task);
	
	public abstract boolean selectTentativeMemberEverySubtask(ConcreteAgent leader, Task task, ArrayList<ConcreteAgent> selectedAgents, ConcreteAgent[] sortedAgents);
	
	public abstract void sendOfferMessageToTentativeMembers(Task task, ConcreteAgent leader);
}
