package fixed.strategy.taskselection;

import fixed.main.TeamFormationMain;
import fixed.task.FixedTask;
import fixed.agent.FixedAgent;

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
