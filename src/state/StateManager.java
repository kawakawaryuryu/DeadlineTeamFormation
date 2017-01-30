package state;

import constant.Constant;
import agent.Agent;

public class StateManager {

	private static int delayTime = Constant.MESSAGE_DELAY;

	/**
	 * 通信遅延があるかどうかで次の状態を変える
	 * @param agent
	 * @param nextState
	 * @param waitingState
	 */
	public static void changeStateConsideringDelay(Agent agent, State nextState, State waitingState) {
		if(delayTime == 0) {
			agent.getParameter().changeState(nextState);
		}
		else {
			agent.getParameter().changeState(waitingState);
		}
	}

}
