package main.model;

import main.teamformation.TeamFormationInstances;
import post.DelayPost;
import post.Post;

public class MessageDelay implements Model {

	public MessageDelay() {
	}

	@Override
	public void run(AgentActionManager action) {
		action.actionByInitialAgent();
		sendOfferMessages();
		sendAnswerMessages();
		sendTeamFormationMessages();
		action.actionByMarkedWatingAgent();
		action.actionByRoleSelectionAgent();
		action.actionByLeaderOrMemberWaitingAgent();
		action.actionByLeaderOrMemberAgent();
		action.actionByExecuteAgent();
	}

	public String toString() {
		return "通信遅延モデル";
	}

	private static void sendOfferMessages() {
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
}
