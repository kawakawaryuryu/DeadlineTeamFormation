package main.model;

import post.DelayPost;
import post.Post;
import main.teamformation.TeamFormationInstances;

public class MessageDelayFailurePenalty implements Model {

	public MessageDelayFailurePenalty() {
	}

	@Override
	public void run(AgentActionManager action) {
		action.actionByInitialAgent();
		sendOfferMessages();
		sendAnswerMessages();
		sendTeamFormationMessages();
		action.actionByReturnedWaitingAgent();
		action.actionByMarkedWatingAgent();
		action.actionByRoleSelectionAgent();
		action.actionByLeaderOrMemberWaitingAgent();
		action.actionByLeaderOrMemberAgent();
		sendSubtaskDoneMessages();
		sendTeamDissolutionMessages();
		action.actionByMemberExecuteAgent();
		action.actionByLeaderExecuteAgent();
		action.actionByMemberTeamDissolutionConfirmationAgent();
	}

	public String toString() {
		return "通信遅延モデル + タスク返却ペナルティモデル";
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
