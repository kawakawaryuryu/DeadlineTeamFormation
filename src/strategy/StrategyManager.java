package strategy;

import strategy.memberselection.ConcreteTentativeMemberSelection;
import strategy.memberselection.ConcreteTentativeMemberSelection2;
import strategy.memberselection.RandomTentativeMemberSelection;
import strategy.memberselection.TentativeMemberSelectionStrategy;
import strategy.roleselection.FixedRoleSelectionStrategy;
import strategy.roleselection.RandomRoleSelectionStrategy;
import strategy.roleselection.RoleSelectionStrategy;
import strategy.subtaskallocation.ConcreteSubtaskAllocationStrategy;
import strategy.subtaskallocation.RandomSubtaskAllocationStrategy;
import strategy.subtaskallocation.SubtaskAllocationStrategy;
import strategy.taskselection.FixedFirstInFirstOutStrategy;
import strategy.taskselection.FixedNoEstimationFirstInFirstOutStrategy;
import strategy.taskselection.FixedTaskSelectionStrategy;

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
	 * 学習あり、学習なし戦略の設定
	 * 仮メンバの選択方法を変える
	 */
	public static void setLearningAndNoLearningStrategyForMemberSelection() {
		roleSelectionStrategy = new RoleSelectionStrategy();
		allocationStrategy = new ConcreteSubtaskAllocationStrategy();
		memberSelectionStrategy = new ConcreteTentativeMemberSelection2();
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
