package message;

import agent.ConcreteAgent;

public class Message {
	/** 誰から */
	protected ConcreteAgent from;
	
	/** 誰に */
	protected ConcreteAgent to;
	
	/** 通信遅延時間 */
	protected int delayTime;
	
	public Message(ConcreteAgent from, ConcreteAgent to){
		this.from = from;
		this.to = to;
	}
	
	public ConcreteAgent getFrom() {
		return from;
	}
	
	public ConcreteAgent getTo() {
		return to;
	}
	
	public int getDelayTime() {
		return delayTime;
		
	}
	
	public String toString() {
		return "from: " + from + " to: " + to;
	}
	
}
