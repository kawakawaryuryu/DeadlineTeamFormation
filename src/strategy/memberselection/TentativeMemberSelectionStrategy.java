package strategy.memberselection;

import java.util.ArrayList;

import task.FixedTask;
import agent.FixedAgent;

public interface TentativeMemberSelectionStrategy {

	public abstract boolean searchTentativeMembers(FixedAgent leader, FixedTask task);
	
	public abstract void pullExecutedSubtaskByLeader(FixedAgent leader, FixedTask task);
	
	public abstract boolean selectTentativeMemberEverySubtask(FixedAgent leader, FixedTask task, ArrayList<FixedAgent> selectedAgents, FixedAgent[] sortedAgents);
	
	public abstract void sendOfferMessageToTentativeMembers(FixedTask task, FixedAgent leader);
}
