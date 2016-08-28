package message;
import agent.Agent;


public class SubtaskDoneMessage extends Message {

	public SubtaskDoneMessage(Agent from, Agent to) {
		super(from, to);
	}

	public SubtaskDoneMessage(Agent from, Agent to, int delayTime) {
		super(from, to, delayTime);
	}

}
