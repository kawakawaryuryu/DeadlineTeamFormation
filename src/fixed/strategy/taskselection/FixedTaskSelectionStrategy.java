package fixed.strategy.taskselection;

import fixed.agent.FixedAgent;
import fixed.task.FixedTask;

public interface FixedTaskSelectionStrategy {

	/**
	 * キュー内のタスクを選択する
	 * @param agent
	 * @return
	 */
	public abstract FixedTask selectTask(FixedAgent agent);
}
