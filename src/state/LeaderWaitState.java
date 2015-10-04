package state;

import main.Constant;
import agent.Agent;

public class LeaderWaitState implements State {

	public static State onlyState = new LeaderWaitState();
	
	/**
	 * エージェントの行動
	 * リーダ提案送信状態において待機する
	 * @param agent
	 */
	@Override
	public void agentAction(Agent leader) {
		leader.addLeaderTimer();
		
		if(leader.getLeaderTimer() == Constant.WAIT_TURN){
			if(leader.getLeaderFields().isTeaming){
				leader.changeState(ExecuteState.getInstance());
			}
			else{
				leader.changeState(InitialMarkingTaskState.getInstance());
			}
		}
		
		else{
			leader.changeState(LeaderWaitState.getInstance());
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
		return "リーダ提案送信待機状態";
	}

}
