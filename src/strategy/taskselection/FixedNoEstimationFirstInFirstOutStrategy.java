package strategy.taskselection;

import task.FixedTask;
import main.TeamFormationMain;
import agent.FixedAgent;

public class FixedNoEstimationFirstInFirstOutStrategy implements
		FixedTaskSelectionStrategy {

	@Override
	public FixedTask selectTask(FixedAgent agent) {
		for(FixedTask task : TeamFormationMain.getParameter().lookingTaskQueue()){
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
