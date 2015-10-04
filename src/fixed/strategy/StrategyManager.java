package fixed.strategy;

import fixed.strategy.memberselection.ConcreteTentativeMemberSelection;
import fixed.strategy.memberselection.TentativeMemberSelectionStrategy;
import fixed.strategy.roleselection.FixedRoleSelectionStrategy;
import fixed.strategy.roleselection.RoleSelectionStrategy;
import fixed.strategy.subtaskallocation.ConcreteSubtaskAllocationStrategy;
import fixed.strategy.subtaskallocation.SubtaskAllocationStrategy;
import fixed.strategy.taskselection.FixedFirstInFirstOutStrategy;
import fixed.strategy.taskselection.FixedTaskSelectionStrategy;

public class StrategyManager {
	
	private static FixedTaskSelectionStrategy taskSelectionStrategy = new FixedFirstInFirstOutStrategy();
	private static FixedRoleSelectionStrategy roleSelectionStrategy = new RoleSelectionStrategy();
	private static SubtaskAllocationStrategy allocationStrategy = new ConcreteSubtaskAllocationStrategy();
	
	private static TentativeMemberSelectionStrategy memberSelectionStrategy = new ConcreteTentativeMemberSelection();
	
	
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
