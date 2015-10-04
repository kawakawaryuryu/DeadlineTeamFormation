package fixed.post;

import fixed.agent.FixedAgent;
import fixed.message.FixedAnswerMessage;
import fixed.message.FixedOfferMessage;
import fixed.message.FixedTeamFormationMessage;

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
