package roleaction;

import state.TaskSelectionState;
import agent.ConcreteAgent;

public class BackToInitialStateAction implements RoleAction {

	@Override
	public void action(ConcreteAgent agent) {
		agent.getParameter().changeState(TaskSelectionState.getState());
	}

}
