package fixed.state;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;

public class WaitingState implements FixedState {
	
	private static FixedState state = new WaitingState();

	@Override
	public void agentAction(FixedAgent agent) {
		// 何もしない
		
		agent.getParameter().countWaitingTime();
		
		if(agent.getParameter().getWaitingStateTimer() < FixedConstant.WAIT_TURN - 1) {
			
		}
		else if(agent.getParameter().getWaitingStateTimer() == FixedConstant.WAIT_TURN - 1) {
			agent.getParameter().changeState(agent.getParameter().getNextState());
		}
		else{
			System.err.println("WaitingStateでこのようなパターンはありえません");
			System.exit(-1);
		}
	}
	
	public static FixedState getState() {
		return state;
	}
	
	public String toString() {
		return "待機状態";
	}

}
