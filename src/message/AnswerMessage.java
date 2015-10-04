package message;

import task.SubTask;
import agent.Agent;

public class AnswerMessage {
	private Agent from;	//誰からの返答か
	private boolean isok;	//チームに参加できるかどうか
	private SubTask subtask;	//提案メッセージに添付されて送られてきたサブタスク
	
	/**
	 * コンストラクタ
	 * @param from
	 * @param isok
	 * @param subtask
	 */
	public AnswerMessage(Agent from, boolean isok, SubTask subtask){
		this.from = from;
		this.isok = isok;
		this.subtask = subtask;
	}
	
	/**
	 * 誰からの返答か返す
	 * @return
	 */
	public Agent getFrom(){
		return from;
	}
	
	/**
	 * チームに参加できるかどうか返す
	 * @return
	 */
	public boolean getIsOk(){
		return isok;
	}
	
	/**
	 * 提案メッセージのときに添付されてきたサブタスクを返す
	 * @return
	 */
	public SubTask getSubTask(){
		return subtask;
	}
	
}
