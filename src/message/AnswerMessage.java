package message;

import task.Subtask;
import agent.ConcreteAgent;

public class AnswerMessage extends Message {
	private boolean isok;
	private Subtask subtask;

	public AnswerMessage(ConcreteAgent from, ConcreteAgent to, boolean isok, Subtask subtask) {
		super(from, to);
		this.isok = isok;
		this.subtask = subtask;
	}
	
	public boolean getIsOk() {
		return isok;
	}
	
	public Subtask getSubtask() {
		return subtask;
	}

}
