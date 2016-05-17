package strategy.taskselection;

import task.Task;
import main.teamformation.TeamFormationInstances;
import agent.Agent;

public class NoEstimationFirstInFirstOutStrategy implements
		TaskSelectionStrategy {

	@Override
	public Task selectTask(Agent agent) {
		for(Task task : TeamFormationInstances.getInstance().getParameter().lookingTaskQueue()){
			if(!task.getMark()){
				return task;
			}
		}
		return null;
	}
	
	public String toString() {
		return "FIFO + 見積もりなし";
	}

}
