package fixed.message;

import fixed.agent.FixedAgent;
import fixed.task.FixedSubtask;

public class FixedOfferMessage extends Message {
	private FixedSubtask subtask;

	public FixedOfferMessage(FixedAgent from, FixedAgent to, FixedSubtask subtask) {
		super(from, to);
		this.subtask = subtask;
	}
	
	public FixedSubtask getSubtask() {
		return subtask;
	}
	
	public String toString() {
		return "from: " + from + " to: " + to + " subtask: " + subtask;
	}
}
