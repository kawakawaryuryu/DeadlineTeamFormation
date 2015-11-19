package fixed.state;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.roleaction.RoleAction;
import fixed.roleaction.TentativeMemberSelectionAction;

public class TaskMarkedWaitingState implements FixedState {

	private static FixedState state = new TaskMarkedWaitingState();
	
	private RoleAction strategy = new TentativeMemberSelectionAction();

	@Override
	public void agentAction(FixedAgent agent) {
		// ntick前　マークしようとしているタスクがマークされているかチェック
		// ntick後　タスクをマークし, 仮メンバ探しをする

		agent.getParameter().getTimerField().countTaskMarkedWaitingStateTimer();

		if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() < FixedConstant.WAIT_TURN) {
			// 何もしない
		}

		else if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() == FixedConstant.WAIT_TURN) {
			// 仮メンバを選択する
			strategy.action(agent);
		}
		
		else{
			System.err.println("TaskMarkedWaitingStateでこのようなパターンはありえません");
			System.exit(-1);
		}
	}
	
	public static FixedState getState() {
		return state;
	}
	
	public String toString() {
		return "タスクマーク状態";
	}

}
