package state;

import constant.Constant;
import exception.AbnormalException;
import agent.Agent;

public class MemberWaitingState implements State {

	private static State state = new MemberWaitingState();

	/**
	 * 通信遅延のため待機する
	 * 時間になったらメンバ状態に移行
	 */
	@Override
	public void agentAction(Agent agent) {

		// タイマーを+1カウント
		agent.getParameter().getTimerField().countMemberWaitingStateTimer();

		if(agent.getParameter().getTimerField().getMemberWaitingStateTimer() < Constant.MESSAGE_DELAY * 2) {
			// 何もしない
		}
		else if(agent.getParameter().getTimerField().getMemberWaitingStateTimer() == Constant.MESSAGE_DELAY * 2) {
			// メンバ状態に移行
			agent.getParameter().changeState(SubtaskReceptionState.getState());
		}
		else {
			throw new AbnormalException("MemberWaitingStateでこのようなパターンはありえません");
		}
	}

	public static State getState() {
		return state;
	}

	public String toString() {
		return "メンバ状態前の待機状態";
	}

}
