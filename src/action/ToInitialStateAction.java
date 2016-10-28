package action;

import state.InitialRoleDecisionState;
import state.TaskSelectionState;
import config.Configuration;
import main.model.InitialRoleDecisionModel;
import main.teamformation.TeamFormationInstances;
import agent.Agent;

public class ToInitialStateAction implements Action {

	@Override
	public void action(Agent agent) {
		if (Configuration.model instanceof InitialRoleDecisionModel) {
			agent.getParameter().changeState(InitialRoleDecisionState.getState());
			TeamFormationInstances.getInstance().getParameter().addAgentToAgentsMap(InitialRoleDecisionState.getState(), agent);
		}
		else {
			agent.getParameter().changeState(TaskSelectionState.getState());
			TeamFormationInstances.getInstance().getParameter().addAgentToAgentsMap(TaskSelectionState.getState(), agent);
		}
	}

}
