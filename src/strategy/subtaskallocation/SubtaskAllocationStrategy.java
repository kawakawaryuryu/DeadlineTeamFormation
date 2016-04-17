package strategy.subtaskallocation;

import task.FixedSubtask;
import agent.FixedAgent;

public interface SubtaskAllocationStrategy {
	
	/**
	 * 仮チームのメンバにサブタスクを振り割る
	 * @param leader
	 */
	public abstract void decideMembers(FixedAgent leader);
	
	/**
	 * 複数のメンバ候補から一人を絞る
	 * @param leader
	 * @param subtask
	 * @return
	 */
	public abstract FixedAgent selectMember(FixedAgent leader, FixedSubtask subtask);
	
	/**
	 * 割り当てが決まっていないサブタスクを割り当てる
	 * @param leader
	 */
	public abstract boolean allocateNotAllocatedSubtask(FixedAgent leader);
}
