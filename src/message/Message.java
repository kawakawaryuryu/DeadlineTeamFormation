package message;

import agent.Agent;

public class Message {
	/** 誰から */
	protected Agent from;
	
	/** 誰に */
	protected Agent to;
	
	/** 通信遅延時間 */
	protected int delayTime;

	protected int delayCount;
	
	public Message(Agent from, Agent to){
		this.from = from;
		this.to = to;
		this.delayTime = 0;
		this.delayCount = 0;
	}

	public Message(Agent from, Agent to, int delayTime) {
		this.delayTime = delayTime;
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

	public void countDelay() {
		delayCount++;
	}

	public int getDelayCount() {
		return delayCount;
	}
	
	public String toString() {
		return "from: " + from + " to: " + to + " delay: " + delayTime;
	}
	
}
