package action;

import main.model.MessageDelayFailurePenalty;
import config.Configuration;
import constant.Constant;
import exception.AbnormalException;
import state.TaskReturnedWaitingState;
import state.TaskSelectionState;
import agent.Agent;

/**
 * タスクを返却するのに一旦返却待機状態にいくかどうか
 */
public class ToTaskReturnedWaitingStateAction implements Action {

	private int waitTurn = Constant.WAIT_TURN;

	@Override
	public void action(Agent agent) {
		if(waitTurn == 0 || !(Configuration.model instanceof MessageDelayFailurePenalty)) {
			// 待機時間（コピー時間）が0の場合はすぐに移行
			agent.getParameter().changeState(TaskSelectionState.getState());

			// タスクマークを外す
			agent.getParameter().getMarkedTask().markingTask(false);
		}
		else if(Configuration.model instanceof MessageDelayFailurePenalty) {
			// 待機状態に移行
			agent.getParameter().changeState(TaskReturnedWaitingState.getState());
		}
		else {
			throw new AbnormalException("ToTaskReturnedWaitingStateActionでこのようなパターンはありえません");
		}
	}

}
