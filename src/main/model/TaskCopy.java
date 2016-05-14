package main.model;


public class TaskCopy implements Model {

	@Override
	public void run(AgentActionManager action) {
		action.actionByInitialAgent();
		action.actionByMarkedWatingAgent();
		action.actionByRoleSelectionAgent();
		action.actionByLeaderOrMemberAgent();
		action.actionByExecuteAgent();
	}

}
