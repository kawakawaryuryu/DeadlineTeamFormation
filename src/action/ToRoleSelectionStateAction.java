package action;

import state.InitialLeaderState;
import state.RoleSelectionState;
import main.model.InitialRoleDecisionModel;
import main.teamformation.TeamFormationInstances;
import config.Configuration;
import agent.Agent;

public class ToRoleSelectionStateAction implements Action {

	@Override
	public void action(Agent agent) {
		if (Configuration.model instanceof InitialRoleDecisionModel) {
			agent.getParameter().changeState(InitialLeaderState.getState());
			TeamFormationInstances.getInstance().getParameter().addAgentToAgentsMap(InitialLeaderState.getState(), agent);
		}
		else {
			agent.getParameter().changeState(RoleSelectionState.getState());
			TeamFormationInstances.getInstance().getParameter().addAgentToAgentsMap(RoleSelectionState.getState(), agent);
		}
	}

}
