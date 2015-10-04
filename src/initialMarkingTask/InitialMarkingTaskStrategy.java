package initialMarkingTask;

import agent.Agent;
import task.Task;

public interface InitialMarkingTaskStrategy {

	/**
	 * キュー内のタスクを選択する
	 * @param agent
	 * @return
	 */
	public abstract Task selectTask(Agent agent);
}
