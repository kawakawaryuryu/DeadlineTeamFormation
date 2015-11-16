package fixed.state;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.main.TeamFormationMain;
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
			if(isMarkedTask(agent)){
				// マークしてある場合はメッセージを確認しにいく
				moveToRoleSelectionState(agent);
			}
			else{
				// 何もしない
			}
		}
		
		else if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() == FixedConstant.WAIT_TURN) {
			if(isMarkedTask(agent)){
				// マークしてある場合はメッセージを確認しにいく
				moveToRoleSelectionState(agent);
			}
			else{
				// タスクをマーク
				agent.getParameter().getMarkedTask().markingTask(true);
				
				// メンバ候補探しにいく
				strategy.action(agent);
			}
		}
		
		else{
			System.err.println("TaskMarkedWaitingStateでこのようなパターンはありえません");
			System.exit(-1);
		}
	}
	
	private boolean isMarkedTask(FixedAgent agent) {
		return agent.getParameter().getMarkedTask().getMark();
	}
	
	private void moveToRoleSelectionState(FixedAgent agent) {
		agent.getParameter().setMarkedTask(null);
		agent.getParameter().changeState(RoleSelectionState.getState());
		TeamFormationMain.getParameter().addAgentToAgentsMap(RoleSelectionState.getState(), agent);
	}
	
	public static FixedState getState() {
		return state;
	}
	
	public String toString() {
		return "タスクマーク状態";
	}

}
