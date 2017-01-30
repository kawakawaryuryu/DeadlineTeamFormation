package action;

import state.InitialRoleDecisionState;
import state.ReciprocalTaskSelectionState;
import state.TaskSelectionState;
import config.Configuration;
import main.model.InitialRoleDecisionModel;
import main.model.MessageDelayByStructuredAgent;
import agent.Agent;

public class ToInitialStateAction implements Action {

	@Override
	public void action(Agent agent) {
		if (Configuration.model instanceof InitialRoleDecisionModel) {
			agent.getParameter().changeState(InitialRoleDecisionState.getState());
		}
		else if (Configuration.model instanceof MessageDelayByStructuredAgent) {
			agent.getParameter().changeState(ReciprocalTaskSelectionState.getState());
		}
		else {
			agent.getParameter().changeState(TaskSelectionState.getState());
		}
	}

}
