package strategy.taskselection;

import java.util.Comparator;

import constant.Constant;
import library.EstimationLibrary;
import main.teamformation.TeamFormationInstances;
import task.Task;
import agent.Agent;

public class MaxEstimationLimitedTasksStrategy implements TaskSelectionStrategy {

	@Override
	public Task selectTask(Agent agent) {
		Task selectedTask = TeamFormationInstances.getInstance().getParameter().taskQueue.stream()
			.filter(task -> !task.getMark()).limit(Constant.ESTIMATION_TASK_LIMIT)
			.filter(task -> EstimationLibrary.canExecuteTaskInTeam(agent, task)).max(Comparator.comparing(Task::getTaskRequireSum)).orElse(null);

		return selectedTask;
	}

	public String toString() {
		return "限られたタスクの中から見積もって処理できる最大のリソースのタスクを選択する";
	}

}
