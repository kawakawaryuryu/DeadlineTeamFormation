package state;

import roleaction.RoleActionManager;
import constant.Constant;
import exception.AbnormalException;
import agent.Agent;

public class TaskMarkedWaitingState implements State {

	private static State state = new TaskMarkedWaitingState();

	@Override
	public void agentAction(Agent agent) {
		// ntick前　マークしようとしているタスクがマークされているかチェック
		// ntick後　タスクをマークし, 仮メンバ探しをする

		agent.getParameter().getTimerField().countTaskMarkedWaitingStateTimer();

		if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() < Constant.WAIT_TURN) {
			// 何もしない
		}

		else if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() == Constant.WAIT_TURN) {
			// 仮メンバを選択する
			RoleActionManager.tentativeMemberSelectionAction.action(agent);
		}
		
		else{
			throw new AbnormalException("TaskMarkedWaitingStateでこのようなパターンはありえません");
		}
	}
	
	public static State getState() {
		return state;
	}
	
	public String toString() {
		return "タスクマーク状態";
	}

}
