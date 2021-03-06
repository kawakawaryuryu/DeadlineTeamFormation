package message;

import task.Subtask;
import agent.Agent;

public class OfferMessage extends Message {
	private Subtask subtask;

	public OfferMessage(Agent from, Agent to, Subtask subtask) {
		super(from, to);
		this.subtask = subtask;
	}

	public OfferMessage(Agent from, Agent to, int delayTime, Subtask subtask) {
		super(from, to, delayTime);
		this.subtask = subtask;
	}
	
	public Subtask getSubtask() {
		return subtask;
	}
	
	public String toString() {
		return "from: " + from + " to: " + to + " subtask: " + subtask;
	}
}
