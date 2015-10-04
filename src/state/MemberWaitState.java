package state;

import main.Constant;
import agent.Agent;

public class MemberWaitState implements State {
	
	public static State onlyState = new MemberWaitState();

	/**
	 * エージェントの行動
	 * メンバ提案受託状態において待機する
	 * @param member
	 */
	@Override
	public void agentAction(Agent member) {
		member.addMemberTimer();
		
		if(member.getMemberTimer() == Constant.WAIT_TURN){
			if(member.getMemberFields().isTeaming){
				member.changeState(ExecuteState.getInstance());
			}
			else{
				member.changeState(InitialMarkingTaskState.getInstance());
			}
		}
		
		else{
			member.changeState(MemberWaitState.getInstance());
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
		return "メンバ提案受託待機状態";
	}

}
