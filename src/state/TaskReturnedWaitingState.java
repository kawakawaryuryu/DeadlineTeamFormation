package state;

import main.teamformation.TeamFormationInstances;
import constant.Constant;
import exception.AbnormalException;
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

			// 現時点はタスクはマークし、チーム編成に成功した場合初めてキューから取り除くので、
			// キューの最後に戻すのは一旦キューから削除し、再度キューの最後に追加するようにしている
			TeamFormationInstances.getInstance().getParameter().removeTask(agent.getParameter().getMarkedTask());
			TeamFormationInstances.getInstance().getParameter().returnTask(agent.getParameter().getMarkedTask());

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
