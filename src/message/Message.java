package message;

import agent.Agent;

public class Message {
	/** 誰から */
	protected Agent from;
	
	/** 誰に */
	protected Agent to;
	
	/** 通信遅延時間 */
	protected int delayTime;
	
	public Message(Agent from, Agent to){
		this.from = from;
		this.to = to;
	}
	
	public Agent getFrom() {
		return from;
	}
	
	public Agent getTo() {
		return to;
	}
	
	public int getDelayTime() {
		return delayTime;
		
	}
	
	public String toString() {
		return "from: " + from + " to: " + to;
	}
	
}
