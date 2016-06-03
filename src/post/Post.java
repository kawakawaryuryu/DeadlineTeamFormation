package post;

import message.AnswerMessage;
import message.OfferMessage;
import message.TeamFormationMessage;
import agent.ConcreteAgent;

public class Post {
	
	public void postOfferMessage(ConcreteAgent to, OfferMessage message) {
		to.getParameter().addOfferMessage(message);
	}
	
	public void postAnswerMessage(ConcreteAgent to, AnswerMessage message) {
		to.getParameter().addAnswerMessage(message);
	}
	
	public void postTeamFormationMessage(ConcreteAgent to, TeamFormationMessage message) {
		to.getParameter().setTeamFormationMessage(message);
	}
}
