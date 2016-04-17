package strategy.taskselection;

import task.Task;
import main.TeamFormationMain;
import agent.Agent;

public class NoEstimationFirstInFirstOutStrategy implements
		TaskSelectionStrategy {

	@Override
	public Task selectTask(Agent agent) {
		for(Task task : TeamFormationMain.getParameter().lookingTaskQueue()){
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
