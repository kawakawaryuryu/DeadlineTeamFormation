package roleaction;

import action.ActionManager;
import agent.Agent;

public class BackToInitialStateAction implements RoleAction {

	@Override
	public void action(Agent agent) {
		ActionManager.toInitialStateAction.action(agent);
	}

}
