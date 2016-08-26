package main.model;

import main.teamformation.TeamFormationInstances;
import post.DelayPost;
import post.Post;

public class MessageDelay implements Model {

	private static Post post;

	public MessageDelay() {
		post = TeamFormationInstances.getInstance().getPost();
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

	private static void sendOfferMessages() {
		if (post instanceof DelayPost) {
			((DelayPost)post).sendOfferMessages();
		}
	}

	private void sendAnswerMessages() {
		if (post instanceof DelayPost) {
			((DelayPost)post).sendAnswerMessages();
		}
	}

	private void sendTeamFormationMessages() {
		if (post instanceof DelayPost) {
			((DelayPost)post).sendTeamFormationMessages();
		}
	}
}
