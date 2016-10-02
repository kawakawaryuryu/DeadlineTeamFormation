package state;

import constant.Constant;
import exception.AbnormalException;
import action.ActionManager;
import agent.Agent;

public class TaskReturnedWaitingState implements State {

	private static State state = new TaskReturnedWaitingState();


	@Override
	public void agentAction(Agent agent) {

		agent.getParameter().getTimerField().countTaskReturnedWaitingStateTimer();

		if(agent.getParameter().getTimerField().getTaskReturnedWaitingStateTimer() < Constant.WAIT_TURN) {
			// 何もしない
		}

		else if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() == Constant.WAIT_TURN) {
			// タスクをキューに返却する

			// サブタスクが保持している情報をクリア
			agent.getParameter().getMarkedTask().clear();

			// タスクからマークを外す
			agent.getParameter().getMarkedTask().markingTask(false);

			// タスクを返却する
			ActionManager.taskReturnAction.action(agent);

			// 初期状態に移行
			agent.getParameter().changeState(TaskSelectionState.getState());
		}

		else{
			throw new AbnormalException("TaskReturnedWaitingStateでこのようなパターンはありえません");
		}
	}

	public static State getState() {
		return state;
	}

	public String toString() {
		return "タスク返却状態";
	}

}
