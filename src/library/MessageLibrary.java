package library;

import constant.Constant;
import agent.Agent;

public class MessageLibrary {


	/**
	 * エージェント間のメッセージ遅延時間を返す
	 * @param from
	 * @param to
	 * @return
	 */
	public static int getMessageTime(Agent from, Agent to) {
		return Constant.MESSAGE_DELAY;
	}

}
