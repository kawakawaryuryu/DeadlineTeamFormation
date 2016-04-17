package roleaction;

import state.TaskMarkedWaitingState;
import agent.Agent;

public class MoveToWaitingAction implements RoleAction {

	@Override
	public void action(Agent agent) {
		// 待機状態に移行
		agent.getParameter().changeState(TaskMarkedWaitingState.getState());

	}

}
