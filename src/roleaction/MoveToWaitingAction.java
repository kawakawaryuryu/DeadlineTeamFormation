package roleaction;

import state.TaskMarkedWaitingState;
import agent.FixedAgent;

public class MoveToWaitingAction implements RoleAction {

	@Override
	public void action(FixedAgent agent) {
		// 待機状態に移行
		agent.getParameter().changeState(TaskMarkedWaitingState.getState());

	}

}
