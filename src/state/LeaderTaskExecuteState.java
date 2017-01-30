package state;

import post.DelayPost;
import post.Post;
import exception.AbnormalException;
import task.Subtask;
import library.MessageLibrary;
import log.Log;
import main.teamformation.TeamFormationInstances;
import message.SubtaskDoneMessage;
import message.TeamDissolutionMessage;
import action.ActionManager;
import agent.Agent;

public class LeaderTaskExecuteState implements State {

	private static State state = new LeaderTaskExecuteState();

	@Override
	public void agentAction(Agent leader) {
		leader.getParameter().getTimerField().countTaskExecuteStateTimer();
		debugExecuteTime(leader);

		// メンバからのサブタスク完了通知確認
		boolean taskDone = confirmMessages(leader);

		// 自分がサブタスク実行中
		if(leader.getParameter().getTimerField().getTaskExecuteStateTimer() <= leader.getParameter().getExecuteTime() &&
				leader.getParameter().getTimerField().getTaskExecuteStateTimer() != leader.getParameter().getParticipatingTeam().getTeamExecuteTime()){
			debugExecutedSubtask(leader);
		}

		// 自分のサブタスクは完了したが、他のメンバが処理している
		else if(leader.getParameter().getExecuteTime() < leader.getParameter().getTimerField().getTaskExecuteStateTimer() &&
				leader.getParameter().getTimerField().getTaskExecuteStateTimer() < leader.getParameter().getParticipatingTeam().getTeamExecuteTime()){
			Log.log.debugln("他のメンバの処理終了待ちです");
		}

		// チーム全員の処理が終了したが、全員分のメッセージを確認できていない
		else if(leader.getParameter().getParticipatingTeam().getTeamExecuteTime() <= leader.getParameter().getTimerField().getTaskExecuteStateTimer() &&
				!taskDone){
			Log.log.debugln("メンバからの終了通知待ちです");
		}

		// チーム全員の処理が終了し、全員分のメッセージを確認できた
		else if(leader.getParameter().getParticipatingTeam().getTeamExecuteTime() <= leader.getParameter().getTimerField().getTaskExecuteStateTimer() &&
				taskDone){
			// 各メンバにチーム解散を通知する
			sendTeamDissolutionMessages(leader);
			ActionManager.toInitialStateAction.action(leader);
			Log.log.debugln("チームの処理が終了しました");
		}

		else{
			throw new AbnormalException("LeaderTaskExecuteStateでこのようなパターンはありえません");
		}

	}

	public static State getState() {
		return state;
	}

	public String toString() {
		return "リーダタスク実行状態";
	}

	/**
	 * メンバからのサブタスク完了メッセージを確認し、全員からきたかどうか判断
	 * @param leader
	 * @return
	 */
	private boolean confirmMessages(Agent leader) {
		for(SubtaskDoneMessage message : leader.getParameter().getSubtaskDoneMessages()) {
			leader.getParameter().getParticipatingTeam().removeMemberForCheck(message.getFrom());
		}

		return leader.getParameter().getParticipatingTeam().isEmptyMembersForCheck();
	}

	private void sendTeamDissolutionMessages(Agent leader) {
		Post post = TeamFormationInstances.getInstance().getPost();
		for(Agent member : leader.getParameter().getParticipatingTeam().getMembers()) {
			int delayTime = MessageLibrary.getMessageTime(leader, member);
			((DelayPost)post).postTeamDissolutionMessage(member, new TeamDissolutionMessage(leader, member, delayTime));
		}
	}

	private void debugExecuteTime(Agent leader) {
		Log.log.debugln("タスク処理時間 = " + leader.getParameter().getExecuteTime());
		Log.log.debugln("チーム処理時間 = " + leader.getParameter().getParticipatingTeam().getTeamExecuteTime());
		Log.log.debugln("実行状態タイマー = " + leader.getParameter().getTimerField().getTaskExecuteStateTimer());
	}

	private void debugExecutedSubtask(Agent leader) {
		Log.log.debugln("以下のサブタスクを処理中です");
		for(Subtask subtask : leader.getParameter().getExecutedSubtasks()){
			Log.log.debugln(subtask);
		}
	}

}
