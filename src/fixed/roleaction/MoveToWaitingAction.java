package fixed.roleaction;

import fixed.agent.FixedAgent;
import fixed.state.TaskMarkedWaitingState;

public class MoveToWaitingAction implements RoleAction {

	@Override
	public void action(FixedAgent agent) {
		// 待機状態に移行
		agent.getParameter().changeState(TaskMarkedWaitingState.getState());

	}

}
