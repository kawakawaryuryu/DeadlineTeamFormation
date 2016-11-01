package main.model;

import main.teamformation.TeamFormationInstances;
import post.Post;
import post.DelayPost;

public class InitialRoleDecisionModel implements Model {

	@Override
	public void run(AgentActionManager action) {
		action.actionByInitialRoleDecisionAgent();
		sendOfferMessages();
		sendAnswerMessages();
		sendTeamFormationMessages();
		action.actionByMarkedWatingAgent();
		action.actionByTaskSelectionAgent();
		action.actionByInitialLeaderAgent();
		action.actionByInitialMemberAgent();
		action.actionByLeaderOrMemberWaitingAgent();
		action.actionByLeaderOrMemberAgent();
		sendSubtaskDoneMessages();
		sendTeamDissolutionMessages();
		action.actionByMemberExecuteAgent();
		action.actionByLeaderExecuteAgent();
		action.actionByMemberTeamDissolutionConfirmationAgent();
	}

	public String toString() {
		return "通信遅延モデル + 役割を最初に決定モデル";
	}

	private void sendOfferMessages() {
		Post post = TeamFormationInstances.getInstance().getPost();
		if (post instanceof DelayPost) {
			((DelayPost)post).sendOfferMessages();
		}
	}

	private void sendAnswerMessages() {
		Post post = TeamFormationInstances.getInstance().getPost();
		if (post instanceof DelayPost) {
			((DelayPost)post).sendAnswerMessages();
		}
	}

	private void sendTeamFormationMessages() {
		Post post = TeamFormationInstances.getInstance().getPost();
		if (post instanceof DelayPost) {
			((DelayPost)post).sendTeamFormationMessages();
		}
	}

	private void sendSubtaskDoneMessages() {
		Post post = TeamFormationInstances.getInstance().getPost();
		if (post instanceof DelayPost) {
			((DelayPost)post).sendSubtaskDoneMessages();
		}
	}

	private void sendTeamDissolutionMessages() {
		Post post = TeamFormationInstances.getInstance().getPost();
		if (post instanceof DelayPost) {
			((DelayPost)post).sendTeamDissolutionMessages();
		}
	}
}
