package main.model;

public class TaskNoCopy implements Model {

	@Override
	public void run(AgentActionManager action) {
		action.actionByInitialAgent();
		action.actionByRoleSelectionAgent();
		action.actionByLeaderOrMemberAgent();
		action.actionByExecuteAgent();
	}

}
