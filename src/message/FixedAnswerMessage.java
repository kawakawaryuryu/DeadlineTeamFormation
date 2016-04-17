package message;

import task.FixedSubtask;
import agent.FixedAgent;

public class FixedAnswerMessage extends Message {
	private boolean isok;
	private FixedSubtask subtask;

	public FixedAnswerMessage(FixedAgent from, FixedAgent to, boolean isok, FixedSubtask subtask) {
		super(from, to);
		this.isok = isok;
		this.subtask = subtask;
	}
	
	public boolean getIsOk() {
		return isok;
	}
	
	public FixedSubtask getSubtask() {
		return subtask;
	}

}
