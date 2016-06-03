package roleaction;

import state.TaskMarkedWaitingState;
import agent.ConcreteAgent;

public class MoveToWaitingAction implements RoleAction {

	@Override
	public void action(ConcreteAgent agent) {
		// 待機状態に移行
		agent.getParameter().changeState(TaskMarkedWaitingState.getState());

	}

}
