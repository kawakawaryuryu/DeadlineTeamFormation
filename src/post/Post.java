package post;

import message.FixedAnswerMessage;
import message.FixedOfferMessage;
import message.FixedTeamFormationMessage;
import agent.FixedAgent;

public class Post {
	
	public void postOfferMessage(FixedAgent to, FixedOfferMessage message) {
		to.getParameter().addOfferMessage(message);
	}
	
	public void postAnswerMessage(FixedAgent to, FixedAnswerMessage message) {
		to.getParameter().addAnswerMessage(message);
	}
	
	public void postTeamFormationMessage(FixedAgent to, FixedTeamFormationMessage message) {
		to.getParameter().setTeamFormationMessage(message);
	}
}
