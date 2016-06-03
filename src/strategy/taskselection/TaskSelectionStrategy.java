package strategy.taskselection;

import task.Task;
import agent.ConcreteAgent;

public interface TaskSelectionStrategy {

	/**
	 * キュー内のタスクを選択する
	 * @param agent
	 * @return
	 */
	public abstract Task selectTask(ConcreteAgent agent);
}
