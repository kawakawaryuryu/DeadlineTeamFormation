package strategy.subtaskallocation;

import task.Subtask;
import agent.ConcreteAgent;

public interface SubtaskAllocationStrategy {
	
	/**
	 * 仮チームのメンバにサブタスクを振り割る
	 * @param leader
	 */
	public abstract void decideMembers(ConcreteAgent leader);
	
	/**
	 * 複数のメンバ候補から一人を絞る
	 * @param leader
	 * @param subtask
	 * @return
	 */
	public abstract ConcreteAgent selectMember(ConcreteAgent leader, Subtask subtask);
	
	/**
	 * 割り当てが決まっていないサブタスクを割り当てる
	 * @param leader
	 */
	public abstract boolean allocateNotAllocatedSubtask(ConcreteAgent leader);
}
