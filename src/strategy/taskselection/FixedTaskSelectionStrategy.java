package strategy.taskselection;

import task.FixedTask;
import agent.FixedAgent;

public interface FixedTaskSelectionStrategy {

	/**
	 * キュー内のタスクを選択する
	 * @param agent
	 * @return
	 */
	public abstract FixedTask selectTask(FixedAgent agent);
}
