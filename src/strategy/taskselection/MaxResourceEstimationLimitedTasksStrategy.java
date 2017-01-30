package strategy.taskselection;

import java.util.Comparator;

import constant.Constant;
import library.EstimationLibrary;
import main.teamformation.TeamFormationInstances;
import task.Task;
import agent.Agent;

public class MaxResourceEstimationLimitedTasksStrategy implements TaskSelectionStrategy {

	/**
	 * 先頭N個のタスクからデッドラインまでに処理可能かつタスクリソースの値が最も大きいタスクを選択する
	 * @param agent
	 */
	@Override
	public Task selectTask(Agent agent) {
		Task selectedTask = TeamFormationInstances.getInstance().getParameter().taskQueue.stream()
			.filter(task -> !task.getMark()).limit(Constant.ESTIMATION_TASK_LIMIT)
			.filter(task -> EstimationLibrary.canExecuteTaskInTeam(agent, task))
			.max(Comparator.comparing(Task::getTaskRequireSum)).orElse(null);

		return selectedTask;
	}

	public String toString() {
		return "限られたタスクの中から見積もって処理できる最大のリソースのタスクを選択する";
	}

}
