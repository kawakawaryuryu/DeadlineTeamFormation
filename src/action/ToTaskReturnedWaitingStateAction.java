package action;

import main.model.MessageDelayFailurePenalty;
import config.Configuration;
import constant.Constant;
import exception.AbnormalException;
import state.TaskReturnedWaitingState;
import state.TaskSelectionState;
import strategy.taskreturn.TaskReturnToLastStrategy;
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

			// タスクを返却する
			// TODO ここでifの条件分岐をしないように外に出す
			if (!(Configuration.taskReturnStrategy instanceof TaskReturnToLastStrategy)) {
				Configuration.taskReturnStrategy.returnTask(agent);
			}
			else {
				throw new AbnormalException("タスク戻し戦略がおかしいです");
			}
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
