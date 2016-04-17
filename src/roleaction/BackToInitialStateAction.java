package roleaction;

import state.TaskSelectionState;
import agent.Agent;

public class BackToInitialStateAction implements RoleAction {

	@Override
	public void action(Agent agent) {
		agent.getParameter().changeState(TaskSelectionState.getState());
	}

}
