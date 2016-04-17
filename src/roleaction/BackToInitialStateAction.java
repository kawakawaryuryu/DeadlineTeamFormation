package roleaction;

import state.TaskSelectionState;
import agent.FixedAgent;

public class BackToInitialStateAction implements RoleAction {

	@Override
	public void action(FixedAgent agent) {
		agent.getParameter().changeState(TaskSelectionState.getState());
	}

}
