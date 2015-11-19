package fixed.state;

import fixed.agent.FixedAgent;
import fixed.roleaction.RoleAction;
import fixed.roleaction.TentativeMemberSelectionAction;

public class TentativeMemberSelectionState implements FixedState {
	
	private static FixedState state = new TentativeMemberSelectionState();
	
	private RoleAction strategy = new TentativeMemberSelectionAction();

	@Override
	public void agentAction(FixedAgent agent) {
		// 仮メンバを選択する
		strategy.action(agent);
	}
	
	public static FixedState getState() {
		return state;
	}
	
	public String toString() {
		return "仮メンバ選択状態";
	}

}
