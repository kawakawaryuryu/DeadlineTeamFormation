package state;

import main.model.MessageDelayFailurePenalty;
import main.teamformation.TeamFormationInstances;
import config.Configuration;
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

		// タスクをキューに返却する（最後に戻す）モデルのとき
		if(Configuration.model instanceof MessageDelayFailurePenalty
				&& agent.getParameter().getTimerField().getMemberWaitingStateTimer() == Constant.WAIT_TURN) {
			// タスクをキューに返却する

			// サブタスクが保持している情報をクリア
			agent.getParameter().getMarkedTask().clear();

			// タスクからマークを外す
			agent.getParameter().getMarkedTask().markingTask(false);

			// 現時点はタスクはマークし、チーム編成に成功した場合初めてキューから取り除くので、
			// キューの最後に戻すのは一旦キューから削除し、再度キューの最後に追加するようにしている
			TeamFormationInstances.getInstance().getParameter().removeTask(agent.getParameter().getMarkedTask());
			TeamFormationInstances.getInstance().getParameter().returnTask(agent.getParameter().getMarkedTask());
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
