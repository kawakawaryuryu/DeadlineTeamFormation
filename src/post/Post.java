package post;

import message.AnswerMessage;
import message.OfferMessage;
import message.TeamFormationMessage;
import agent.Agent;

public class Post {

	public void initialize() {

	}
	
	public void postOfferMessage(Agent to, OfferMessage message) {
		to.getParameter().addOfferMessage(message);
	}
	
	public void postAnswerMessage(Agent to, AnswerMessage message) {
		to.getParameter().addAnswerMessage(message);
	}
	
	public void postTeamFormationMessage(Agent to, TeamFormationMessage message) {
		to.getParameter().setTeamFormationMessage(message);
	}
}
