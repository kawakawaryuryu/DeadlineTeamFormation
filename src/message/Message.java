package message;

import agent.FixedAgent;

public class Message {
	/** 誰から */
	protected FixedAgent from;
	
	/** 誰に */
	protected FixedAgent to;
	
	/** 通信遅延時間 */
	protected int delayTime;
	
	public Message(FixedAgent from, FixedAgent to){
		this.from = from;
		this.to = to;
	}
	
	public FixedAgent getFrom() {
		return from;
	}
	
	public FixedAgent getTo() {
		return to;
	}
	
	public int getDelayTime() {
		return delayTime;
		
	}
	
	public String toString() {
		return "from: " + from + " to: " + to;
	}
	
}
