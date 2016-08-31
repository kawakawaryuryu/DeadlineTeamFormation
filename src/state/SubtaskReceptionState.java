package state;

import role.Role;
import task.Subtask;
import log.Log;
import message.TeamFormationMessage;
import agent.Agent;
import agent.StructuredAgent;

public class SubtaskReceptionState implements State {
	
	private static State state = new SubtaskReceptionState();

	@Override
	public void agentAction(Agent member) {
		// チーム編成成否メッセージの取得
		TeamFormationMessage message = member.getParameter().getTeamFormationMessage();

		member.getParameter().getMemberField().isTeaming = message.getIsOk();
		
		// チーム編成成功の場合
		if(message.getIsOk()){
			// メッセージから得られた情報をセット
			setInfoFromMessage(member, message);
			
			// データの計測
			member.getMeasure().countInMemberSuccessCase(member.getParameter().getElement(Role.MEMBER), member, 
					member.getParameter().getParticipatingTeam());
			
			// 状態遷移
			member.getParameter().changeState(MemberTaskExecuteState.getState());
			
			// 報酬期待度のフィードバック
			member.feedbackExpectedReward(message.getFrom(), true, message.getSubtaskRequireSum(), 
					message.getLeftReward(), message.getLeftRequireSum());
			
			// リーダに対する信頼度のフィードバック
			if (member instanceof StructuredAgent) {
				((StructuredAgent)member).feedbackTrustToLeader(message.getFrom(), message.getTeam(), true);
			}

			Log.log.debugln("チーム編成成功メッセージを受信しました");
			debugExecutedSubtask(member);
		}
		// チーム編成失敗の場合
		else{
			Log.log.debugln("チーム編成失敗メッセージを受信しました");
			// 状態遷移;
			member.getParameter().changeState(TaskSelectionState.getState());
			
			// 報酬期待度のフィードバック
			member.feedbackExpectedReward(message.getFrom(), false, 0, 0, 1);
			
			// リーダに対する信頼度のフィードバック
			if (member instanceof StructuredAgent) {
				((StructuredAgent)member).feedbackTrustToLeader(message.getFrom(), message.getTeam(), false);
			}
		}
	}
	
	private void setInfoFromMessage(Agent member, TeamFormationMessage message) {
		// 処理するサブタスクをセット
		/*for(Subtask subtask : message.getSubtasks()){
			member.getParameter().setExecutedSubtasks(subtask, AgentTaskLibrary.calculateExecuteTime(member, subtask));
		}*/
		
		// チーム情報を保持
		member.getParameter().setParticipatingTeam(message.getTeam());
		
	}
	
	private void debugExecutedSubtask(Agent member) {
		Log.log.debugln("処理するサブタスク");
		for(Subtask subtask : member.getParameter().getExecutedSubtasks()){
			Log.log.debugln(subtask);
		}
	}

	public static State getState() {
		return state;
	}
	
	public String toString() {
		return "サブタスク受け取り状態（メンバ）";
	}

}
