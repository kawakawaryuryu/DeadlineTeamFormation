package fixed.state;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;

public class TaskUnmarkedWaitingState implements FixedState {
	
	private static FixedState state = new TaskUnmarkedWaitingState();

	@Override
	public void agentAction(FixedAgent agent) {
		// ntick前　タスクのマークを外すまで待機
		// ntick経過 タスクのマークを外し、初期状態へ

		agent.getParameter().getTimerField().countTaskUnmarkedWaitingStateTimer();
		
		if(agent.getParameter().getTimerField().getTaskUnmarkedWaitingStateTimer() < FixedConstant.WAIT_TURN - 1){
			// 何もしない
		}
		else if(agent.getParameter().getTimerField().getTaskUnmarkedWaitingStateTimer() == FixedConstant.WAIT_TURN - 1){
			agent.getParameter().getMarkedTask().markingTask(false);
			agent.getParameter().changeState(TaskSelectionState.getState());
		}
		else{
			System.err.println("TaskUnmarkedWaitingStateでこのようなパターンはありえません");
			System.exit(-1);
		}
	}
	
	public static FixedState getState() {
		return state;
	}
	
	public String toString() {
		return "タスクマーク外し状態";
	}

}
