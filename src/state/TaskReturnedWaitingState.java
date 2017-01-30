package state;

import config.Configuration;
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

			// タスクを返却する
			Configuration.taskReturnStrategy.returnTask(agent);

			// 初期状態に移行
			ActionManager.toInitialStateAction.action(agent);
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
