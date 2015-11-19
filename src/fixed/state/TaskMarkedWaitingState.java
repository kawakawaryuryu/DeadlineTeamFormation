package fixed.state;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.main.TeamFormationMain;

public class TaskMarkedWaitingState implements FixedState {
	
	private static FixedState state = new TaskMarkedWaitingState();;

	@Override
	public void agentAction(FixedAgent agent) {
		// ntick前　マークしようとしているタスクがマークされているかチェック
		// ntick後　タスクをマークし, 仮メンバ探しをする
		
		agent.getParameter().getTimerField().countTaskMarkedWaitingStateTimer();
		
		if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() < FixedConstant.WAIT_TURN) {
			if(isMarkedTask(agent)){
				// マークしてある場合はメッセージを確認しにいく
				moveToRoleSelectionStateForMember(agent);
			}
			else{
				// 何もしない
			}
		}
		
		else if(agent.getParameter().getTimerField().getTaskMarkedWaitingStateTimer() == FixedConstant.WAIT_TURN) {
			if(isMarkedTask(agent)){
				// マークしてある場合はメッセージを確認しにいく
				moveToRoleSelectionStateForMember(agent);
			}
			else{
				// タスクをマーク
				agent.getParameter().getMarkedTask().markingTask(true);
				
				// リーダとして役割選択状態に
				moveToTentativeMemberSelectionState(agent);
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
	
	private void moveToRoleSelectionStateForMember(FixedAgent agent) {
		agent.getParameter().setMarkedTask(null);
		agent.getParameter().changeState(RoleSelectionState.getState());
		TeamFormationMain.getParameter().addAgentToAgentsMap(RoleSelectionState.getState(), agent);
	}
	
	private void moveToTentativeMemberSelectionState(FixedAgent agent) {
		agent.getParameter().changeState(TentativeMemberSelectionState.getState());
		TeamFormationMain.getParameter().addAgentToAgentsMap(TentativeMemberSelectionState.getState(), agent);
	}
	
	public static FixedState getState() {
		return state;
	}
	
	public String toString() {
		return "タスクマーク状態";
	}

}
