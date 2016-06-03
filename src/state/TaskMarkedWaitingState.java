package state;

import roleaction.RoleAction;
import roleaction.TentativeMemberSelectionAction;
import constant.Constant;
import agent.ConcreteAgent;

public class TaskMarkedWaitingState implements State {

	private static State state = new TaskMarkedWaitingState();
	
	private RoleAction strategy = new TentativeMemberSelectionAction();

	@Override
	public void agentAction(ConcreteAgent agent) {
		// ntick前　マークしようとしているタスクがマークされているかチェック
		// ntick後　タスクをマークし, 仮メンバ探しをする

		agent.getParameter().getTimerField().countTaskMarkedWaitingStateTimer();

		if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() < Constant.WAIT_TURN) {
			// 何もしない
		}

		else if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() == Constant.WAIT_TURN) {
			// 仮メンバを選択する
			strategy.action(agent);
		}
		
		else{
			System.err.println("TaskMarkedWaitingStateでこのようなパターンはありえません");
			System.exit(-1);
		}
	}
	
	public static State getState() {
		return state;
	}
	
	public String toString() {
		return "タスクマーク状態";
	}

}
