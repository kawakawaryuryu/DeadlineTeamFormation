package strategy.taskselection;

import constant.Constant;
import library.EstimationLibrary;
import main.teamformation.TeamFormationInstances;
import task.Task;
import agent.Agent;

public class MaxEstimationLimitedTasksStrategy implements TaskSelectionStrategy {

	@Override
	public Task selectTask(Agent agent) {
		Task selectedTask = null;
		for(Task task : TeamFormationInstances.getInstance().getParameter().lookingLimitedTaskQueue(0, Constant.ESTIMATION_TASK_LIMIT)){
			if(!task.getMark()){
				if(EstimationLibrary.canExecuteTaskInTeam(agent, task)){
					if (selectedTask == null || (selectedTask != null && selectedTask.getTaskRequireSum() < task.getTaskRequireSum())) {
						selectedTask = task;
					}
				}
			}
		}
		return selectedTask;
	}

	public String toString() {
		return "限られたタスクの中から見積もって処理できる最大のリソースのタスクを選択する";
	}

}
