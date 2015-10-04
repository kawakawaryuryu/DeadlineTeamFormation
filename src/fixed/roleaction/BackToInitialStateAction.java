package fixed.roleaction;

import fixed.agent.FixedAgent;
import fixed.state.TaskSelectionState;

public class BackToInitialStateAction implements RoleAction {

	@Override
	public void action(FixedAgent agent) {
		agent.getParameter().changeState(TaskSelectionState.getState());
	}

}
