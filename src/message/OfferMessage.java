package message;

import task.Subtask;
import agent.ConcreteAgent;

public class OfferMessage extends Message {
	private Subtask subtask;

	public OfferMessage(ConcreteAgent from, ConcreteAgent to, Subtask subtask) {
		super(from, to);
		this.subtask = subtask;
	}
	
	public Subtask getSubtask() {
		return subtask;
	}
	
	public String toString() {
		return "from: " + from + " to: " + to + " subtask: " + subtask;
	}
}
