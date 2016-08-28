package state;

import post.DelayPost;
import task.Subtask;
import library.MessageLibrary;
import log.Log;
import main.teamformation.TeamFormationInstances;
import message.SubtaskDoneMessage;
import exception.AbnormalException;
import agent.Agent;

public class MemberTaskExecuteState implements State {

	private static State state = new MemberTaskExecuteState();

	@Override
	public void agentAction(Agent member) {
		member.getParameter().getTimerField().countTaskExecuteStateTimer();
		debugExecuteTime(member);

		// リーダからのチーム解散通知確認
		boolean disolved = hasTeamDissolutionMessage(member);

		// 自分がサブタスク実行中
		if(member.getParameter().getTimerField().getTaskExecuteStateTimer() <= member.getParameter().getExecuteTime() &&
				member.getParameter().getTimerField().getTaskExecuteStateTimer() != member.getParameter().getParticipatingTeam().getTeamExecuteTime()){
			debugExecutedSubtask(member);
		}

		// 自分のサブタスクが完了
		else if(member.getParameter().getExecuteTime() == member.getParameter().getTimerField().getTaskExecuteStateTimer()){
			// サブタスク完了通知をリーダに送る
			sendSubtaskDoneMessage(member);
			Log.log.debugln("サブタスクの処理が完了しました");
		}

		// チーム解散の通知が来た
		else if(member.getParameter().getExecuteTime() < member.getParameter().getTimerField().getTaskExecuteStateTimer() &&
				disolved){
			member.getParameter().changeState(TaskSelectionState.getState());
			Log.log.debugln("チームの処理が終了しました");
		}

		else{
			throw new AbnormalException("MemberTaskExecuteStateでこのようなパターンはありえません");
		}
	}

	public static State getState() {
		return state;
	}

	public String toString() {
		return "メンバタスク実行状態";
	}

	/**
	 * チーム解散メッセージを受け取ったか確認
	 * @param member
	 * @return
	 */
	private boolean hasTeamDissolutionMessage(Agent member) {
		if(member.getParameter().getTeamDissolutionMessage() != null) return true;
		else return false;
	}

	private void sendSubtaskDoneMessage(Agent member) {
		DelayPost post = (DelayPost)TeamFormationInstances.getInstance().getPost();
		Agent leader = member.getParameter().getTeamFormationMessage().getFrom();
		int delayTime = MessageLibrary.getMessageTime(member, leader);
		post.postSubtaskDoneMessage(leader, new SubtaskDoneMessage(member, leader, delayTime));
	}

	private void debugExecuteTime(Agent member) {
		Log.log.debugln("タスク処理時間 = " + member.getParameter().getExecuteTime());
		Log.log.debugln("チーム処理時間 = " + member.getParameter().getParticipatingTeam().getTeamExecuteTime());
		Log.log.debugln("実行状態タイマー = " + member.getParameter().getTimerField().getTaskExecuteStateTimer());
	}

	private void debugExecutedSubtask(Agent member) {
		Log.log.debugln("以下のサブタスクを処理中です");
		for(Subtask subtask : member.getParameter().getExecutedSubtasks()){
			Log.log.debugln(subtask);
		}
	}

}
