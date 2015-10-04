package state;

import main.Constant;
import message.TeamingMessage;
import task.SubTask;
import agent.Agent;

public class MemberState implements State {
	private static State onlyState = new MemberState();	//状態を保持する（singletonパターン）
	private TeamingMessage message;	//チーム編成成功かどうかのメッセージ
	
	/**
	 * エージェントの行動
	 */
	@Override
	public void agentAction(Agent member){
//		System.out.println("チーム参加成否メッセージを取得します");
		message = member.getTeamingMessage();
		
		/* 報酬期待度のフィードバック */
//		System.out.println("from: " + message.getFrom());
//		System.out.println("isOk: " + message.getIsOk());
//		for(SubTask subtask : message.getSubTaskList()){
//			System.out.println("subtask: " + subtask);
//		}
//		System.out.println("leftReward: " + message.getLeftReward());
//		System.out.println("leftRequireSum: " + message.getLeftRequireSum());
//		System.out.println("Team Information: " + message.getTeamInfo());
		
//		System.out.println(member + " のメッセージ添付のチーム情報 " + message.getTeamInfo());
		member.haveTeamInfo(message.getTeamInfo());	//チーム情報を保持
		
		member.feedbackExpectedReward(message.getFrom(), message.getIsOk(), message.getSubTaskRequireSum(), message.getLeftReward(), message.getLeftRequireSum());
		
		/* チーム参加成否メッセージがOKの場合 */
		if(message.getIsOk() == true){
//			System.out.println("処理すべきサブタスクを保持しました");
			for(SubTask subtask : message.getSubTaskList()){
				member.addExecutedSubTaskList(subtask);
			}
			
			member.addMemberNum();	//メンバの回数を増やす
			member.addTeamingWithLeaderNum(member.getTeamInfo().getLeader());	//リーダとのチーム編成回数を増やす
			member.getMemberFields().isTeaming = true;
			
			member.addTeamingSuccessNum();
			member.addConstraintTimeSumPerAgent(member.getTeamInfo().getExecuteTime() - member.getExecuteTime());
			
			if(member.getTurn() >= Constant.TURN_NUM - Constant.MEASURE_SUCCESS_AT_END_TURN_NUM){
				member.addTeamingSuccessAtEnd();	//最後の数ターンのチーム編成成功回数を増やす
				member.addExecuteStateExecutingTimeAtEnd(member.getExecuteTime());	//処理時間をカウント
				member.addExecuteStateConstraintTimeAtEnd(member.getTeamInfo().getExecuteTime() - member.getExecuteTime());	//無駄に拘束されている時間をカウント
			}
		}
		
		/* チーム参加成否メッセージがNGの場合 */
		else{
			member.getMemberFields().isTeaming = false;
		}
		member.changeState(MemberWaitState.getInstance());	//チーム編成成功の場合、タスク実行状態に移行
		
		member.addMemberTimer();
		member.addMemberStateTime();
	}
	
	public static State getInstance(){
		return onlyState;
	}
	
	public String toString(){
		return "メンバ提案受託状態";
	}

}
