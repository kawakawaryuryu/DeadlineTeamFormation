package message;

import task.SubTask;
import agent.Agent;

public class OfferMessage {
	private Agent from; // 誰からのメッセージか
	private SubTask subtask; // 添付されたサブタスク

	/**
	 * コンストラクタ
	 * @param from
	 * @param subtask
	 */
	public OfferMessage(Agent from, SubTask subtask) {
		this.from = from;
		this.subtask = subtask;
	}

	/**
	 * 送信元を返す
	 * @return
	 */
	public Agent getFrom() {
		return from;
	}

	/**
	 * 添付サブタスクを返す
	 * @return
	 */
	public SubTask getSubTask() {
		return subtask;
	}

}
