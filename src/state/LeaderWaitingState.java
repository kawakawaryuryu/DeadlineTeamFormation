package state;

import constant.Constant;
import exception.AbnormalException;
import agent.Agent;

public class LeaderWaitingState implements State {

	private static State state = new LeaderWaitingState();

	/**
	 * 通信遅延のため待機する
	 * 時間になったらリーダ状態に移行
	 */
	@Override
	public void agentAction(Agent agent) {

		// タイマーを+1カウント
		agent.getParameter().getTimerField().countLeaderWaitingStateTimer();

		if(agent.getParameter().getTimerField().getLeaderWaitingStateTimer() < Constant.MESSAGE_DELAY * 2) {
			// 何もしない
		}
		else if(agent.getParameter().getTimerField().getLeaderWaitingStateTimer() == Constant.MESSAGE_DELAY * 2) {
			// リーダ状態に移行
			agent.getParameter().changeState(SubtaskAllocationState.getState());
		}
		else {
			throw new AbnormalException("LeaderWaitingStateでこのようなパターンはありえません");
		}
	}

	public static State getState() {
		return state;
	}

	public String toString() {
		return "リーダ状態前の待機状態";
	}

}
