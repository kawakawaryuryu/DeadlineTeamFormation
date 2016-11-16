package strategy;

import strategy.memberselection.ConcreteTentativeMemberSelection;
import strategy.memberselection.ConcreteTentativeMemberSelection2;
import strategy.memberselection.RandomTentativeMemberSelection;
import strategy.memberselection.TentativeMemberSelectionStrategy;
import strategy.roleselection.ReciprocalRoleSelectionStrategy;
import strategy.roleselection.RoleSelectionStrategy;
import strategy.roleselection.RandomRoleSelectionStrategy;
import strategy.roleselection.RationalRoleSelectionStrategy;
import strategy.subtaskallocation.ConcreteSubtaskAllocationStrategy;
import strategy.subtaskallocation.RandomSubtaskAllocationStrategy;
import strategy.subtaskallocation.SubtaskAllocationStrategy;
import strategy.taskselection.FirstInFirstOutStrategy;
import strategy.taskselection.MaxEstimationLimitedTasksStrategy;
import strategy.taskselection.MaxEstimationStrategy;
import strategy.taskselection.MaxResourceEstimationLimitedTasksStrategy;
import strategy.taskselection.NoEstimationFirstInFirstOutStrategy;
import strategy.taskselection.TaskSelectionStrategy;

public class StrategyManager {
	
	private static TaskSelectionStrategy taskSelectionStrategy;
	private static RoleSelectionStrategy roleSelectionStrategy;
	private static SubtaskAllocationStrategy allocationStrategy;
	private static TentativeMemberSelectionStrategy memberSelectionStrategy;
	
	// 学習あり＋見積もりあり、学習なし＋見積もりあり
//	private static TaskSelectionStrategy taskSelectionStrategy = new FirstInFirstOutStrategy();
//	private static RoleSelectionStrategy roleSelectionStrategy = new RoleSelectionStrategy();
//	private static SubtaskAllocationStrategy allocationStrategy = new ConcreteSubtaskAllocationStrategy();
//	private static TentativeMemberSelectionStrategy memberSelectionStrategy = new ConcreteTentativeMemberSelection();
	
	// 学習あり＋見積もりなし
//	private static TaskSelectionStrategy taskSelectionStrategy = new NoEstimationFirstInFirstOutStrategy();
//	private static RoleSelectionStrategy roleSelectionStrategy = new RoleSelectionStrategy();
//	private static SubtaskAllocationStrategy allocationStrategy = new ConcreteSubtaskAllocationStrategy();
//	private static TentativeMemberSelectionStrategy memberSelectionStrategy = new ConcreteTentativeMemberSelection();
	
	// ランダム＋見積もりなし
//	private static TaskSelectionStrategy taskSelectionStrategy = new NoEstimationFirstInFirstOutStrategy();
//	private static RoleSelectionStrategy roleSelectionStrategy = new RandomRoleSelectionStrategy();
//	private static SubtaskAllocationStrategy allocationStrategy = new RandomSubtaskAllocationStrategy();
//	private static TentativeMemberSelectionStrategy memberSelectionStrategy = new RandomTentativeMemberSelection();
	
	/**
	 * 見積もりあり戦略の設定
	 */
	public static void setEstimationStrategy() {
		taskSelectionStrategy = new FirstInFirstOutStrategy();
		//taskSelectionStrategy = new MaxEstimationStrategy();
		//taskSelectionStrategy = new MaxEstimationLimitedTasksStrategy();
		//taskSelectionStrategy = new MaxResourceEstimationLimitedTasksStrategy();
	}
	
	/**
	 * 見積もりなし戦略の設定
	 */
	public static void setNoEstimationStrategy() {
		taskSelectionStrategy = new NoEstimationFirstInFirstOutStrategy();
	}
	
	/**
	 * 学習あり、学習なし戦略の設定
	 */
	public static void setLearningAndNoLearningStrategy() {
		roleSelectionStrategy = new RationalRoleSelectionStrategy();
		allocationStrategy = new ConcreteSubtaskAllocationStrategy();
		memberSelectionStrategy = new ConcreteTentativeMemberSelection();
	}
	
	/**
	 * 学習あり、学習なし戦略の設定
	 * 仮メンバの選択方法を変える
	 */
	public static void setLearningAndNoLearningStrategyForMemberSelection() {
		roleSelectionStrategy = new RationalRoleSelectionStrategy();
		allocationStrategy = new ConcreteSubtaskAllocationStrategy();
		memberSelectionStrategy = new ConcreteTentativeMemberSelection2();
	}
	
	/**
	 * 信頼エージェントを持たせる戦略の設定
	 * リーダの選択方法が異なる
	 */
	public static void setReciprocalStrategy() {
		roleSelectionStrategy = new ReciprocalRoleSelectionStrategy();
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
	
	
	public static TaskSelectionStrategy getTaskSelectionStrategy() {
		return taskSelectionStrategy;
	}
	
	public static RoleSelectionStrategy getRoleSelectionStrategy() {
		return roleSelectionStrategy;
	}
	
	public static SubtaskAllocationStrategy getAllocationStrategy() {
		return allocationStrategy;
	}
	
	public static TentativeMemberSelectionStrategy getMemberSelectionStrategy() {
		return memberSelectionStrategy;
	}

}
