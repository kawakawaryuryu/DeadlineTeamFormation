package strategy.taskselection;

import library.EstimationLibrary;
import main.teamformation.TeamFormationInstances;
import task.Task;
import agent.Agent;

public class MaxEstimationStrategy implements TaskSelectionStrategy {

	/**
	 * タスクを先頭から最後まで見ていって見積もったタスクの中から処理できる最大のリソースのタスクを選択する
	 * 処理できるタスクがない場合はnullを返す
	 * @param agent
	 * @return
	 */
	@Override
	public Task selectTask(Agent agent) {
		Task selectedTask = null;
		for(Task task : TeamFormationInstances.getInstance().getParameter().lookingTaskQueue()){
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
		return "見積もって処理できる最大のリソースのタスクを選択する";
	}

}
