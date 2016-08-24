package message;

import task.Subtask;
import agent.Agent;

public class AnswerMessage extends Message {
	private boolean isok;
	private Subtask subtask;

	public AnswerMessage(Agent from, Agent to, boolean isok, Subtask subtask) {
		super(from, to);
		this.isok = isok;
		this.subtask = subtask;
	}

	public AnswerMessage(Agent from, Agent to, int delayTime, boolean isok, Subtask subtask) {
		super(from, to, delayTime);
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
