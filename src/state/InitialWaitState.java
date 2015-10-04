package state;

import main.Constant;
import agent.Agent;

public class InitialWaitState implements State {
	
	public static State onlyState = new InitialWaitState();	//状態

	/**
	 * エージェントの行動
	 * 初期状態において、待機する
	 * @param agent
	 */
	@Override
	public void agentAction(Agent agent) {
		agent.addInitialTimer();
		
		if(agent.getInitialTimer() == Constant.WAIT_TURN){
			if(agent.getRole() == Constant.LEADER_STATE){
				agent.changeState(LeaderState.getInstance());
			}
			else if(agent.getRole() == Constant.MEMBER_STATE){
				agent.changeState(MemberState.getInstance());
			}
			else if(agent.getRole() == Constant.INITIAL_STATE){
				agent.changeState(InitialMarkingTaskState.getInstance());
			}
		}
		
		else{
			agent.changeState(InitialWaitState.getInstance());
		}
	}
	
	/**
	 * 状態インスタンスを返す
	 * @return
	 */
	public static State getInstance(){
		return onlyState;
	}
	
	/**
	 * 状態を返す
	 */
	public String toString(){
		return "初期待機状態";
	}

}
