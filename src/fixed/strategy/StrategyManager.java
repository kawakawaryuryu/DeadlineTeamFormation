package fixed.strategy;

import fixed.strategy.memberselection.ConcreteTentativeMemberSelection;
import fixed.strategy.memberselection.RandomTentativeMemberSelection;
import fixed.strategy.memberselection.TentativeMemberSelectionStrategy;
import fixed.strategy.roleselection.FixedRoleSelectionStrategy;
import fixed.strategy.roleselection.RandomRoleSelectionStrategy;
import fixed.strategy.roleselection.RoleSelectionStrategy;
import fixed.strategy.subtaskallocation.ConcreteSubtaskAllocationStrategy;
import fixed.strategy.subtaskallocation.RandomSubtaskAllocationStrategy;
import fixed.strategy.subtaskallocation.SubtaskAllocationStrategy;
import fixed.strategy.taskselection.FixedFirstInFirstOutStrategy;
import fixed.strategy.taskselection.FixedNoEstimationFirstInFirstOutStrategy;
import fixed.strategy.taskselection.FixedTaskSelectionStrategy;

public class StrategyManager {
	
	private static FixedTaskSelectionStrategy taskSelectionStrategy;
	private static FixedRoleSelectionStrategy roleSelectionStrategy;
	private static SubtaskAllocationStrategy allocationStrategy;
	private static TentativeMemberSelectionStrategy memberSelectionStrategy;
	
	// 学習あり＋見積もりあり、学習なし＋見積もりあり
//	private static FixedTaskSelectionStrategy taskSelectionStrategy = new FixedFirstInFirstOutStrategy();
//	private static FixedRoleSelectionStrategy roleSelectionStrategy = new RoleSelectionStrategy();
//	private static SubtaskAllocationStrategy allocationStrategy = new ConcreteSubtaskAllocationStrategy();
//	private static TentativeMemberSelectionStrategy memberSelectionStrategy = new ConcreteTentativeMemberSelection();
	
	// 学習あり＋見積もりなし
//	private static FixedTaskSelectionStrategy taskSelectionStrategy = new FixedNoEstimationFirstInFirstOutStrategy();
//	private static FixedRoleSelectionStrategy roleSelectionStrategy = new RoleSelectionStrategy();
//	private static SubtaskAllocationStrategy allocationStrategy = new ConcreteSubtaskAllocationStrategy();
//	private static TentativeMemberSelectionStrategy memberSelectionStrategy = new ConcreteTentativeMemberSelection();
	
	// ランダム＋見積もりなし
//	private static FixedTaskSelectionStrategy taskSelectionStrategy = new FixedNoEstimationFirstInFirstOutStrategy();
//	private static FixedRoleSelectionStrategy roleSelectionStrategy = new RandomRoleSelectionStrategy();
//	private static SubtaskAllocationStrategy allocationStrategy = new RandomSubtaskAllocationStrategy();
//	private static TentativeMemberSelectionStrategy memberSelectionStrategy = new RandomTentativeMemberSelection();
	
	/**
	 * 見積もりあり戦略の設定
	 */
	public static void setEstimationStrategy() {
		taskSelectionStrategy = new FixedFirstInFirstOutStrategy();
	}
	
	/**
	 * 見積もりなし戦略の設定
	 */
	public static void setNoEstimationStrategy() {
		taskSelectionStrategy = new FixedNoEstimationFirstInFirstOutStrategy();
	}
	
	/**
	 * 学習あり、学習なし戦略の設定
	 */
	public static void setLearningAndNoLearningStrategy() {
		roleSelectionStrategy = new RoleSelectionStrategy();
		allocationStrategy = new ConcreteSubtaskAllocationStrategy();
		memberSelectionStrategy = new ConcreteTentativeMemberSelection();
	}
	
	/**
	 * ランダム戦略の設定
	 */
	public static void setRandomStrategy() {
		roleSelectionStrategy = new RandomRoleSelectionStrategy();
		allocationStrategy = new RandomSubtaskAllocationStrategy();
		memberSelectionStrategy = new RandomTentativeMemberSelection();
	}
	
	
	public static FixedTaskSelectionStrategy getTaskSelectionStrategy() {
		return taskSelectionStrategy;
	}
	
	public static FixedRoleSelectionStrategy getRoleSelectionStrategy() {
		return roleSelectionStrategy;
	}
	
	public static SubtaskAllocationStrategy getAllocationStrategy() {
		return allocationStrategy;
	}
	
	public static TentativeMemberSelectionStrategy getMemberSelectionStrategy() {
		return memberSelectionStrategy;
	}

}
