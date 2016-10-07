package state;

import main.model.MessageDelayFailurePenalty;
import config.Configuration;
import constant.Constant;
import exception.AbnormalException;
import action.ActionManager;
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

		// タスクをキューに返却する（最後に戻す）モデルのとき
		if(Configuration.model instanceof MessageDelayFailurePenalty
				&& agent.getParameter().getTimerField().getMemberWaitingStateTimer() == Constant.WAIT_TURN
				&& agent.getParameter().getMarkedTask() != null) {

			// タスクをキューに返却する

			// サブタスクが保持している情報をクリア
			agent.getParameter().getMarkedTask().clear();

			// タスクからマークを外す
			agent.getParameter().getMarkedTask().markingTask(false);

			// タスクを返却する
			ActionManager.taskReturnAction.action(agent);
		}

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
