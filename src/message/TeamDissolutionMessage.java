package message;

import agent.Agent;

public class TeamDissolutionMessage extends Message {

	public TeamDissolutionMessage(Agent from, Agent to) {
		super(from, to);
	}

	public TeamDissolutionMessage(Agent from, Agent to, int delayTime) {
		super(from, to, delayTime);
	}

}
