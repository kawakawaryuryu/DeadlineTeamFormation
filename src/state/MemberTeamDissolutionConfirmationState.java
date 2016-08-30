package state;

import exception.AbnormalException;
import log.Log;
import agent.Agent;

public class MemberTeamDissolutionConfirmationState implements State {

	private static State state = new MemberTeamDissolutionConfirmationState();

	@Override
	public void agentAction(Agent member) {

		// リーダからのチーム解散通知確認
		boolean disolved = hasTeamDissolutionMessage(member);

		// リーダからチーム解散の通知が来た
		if(member.getParameter().getExecuteTime() <= member.getParameter().getTimerField().getTaskExecuteStateTimer() && disolved){
			member.getParameter().changeState(TaskSelectionState.getState());
			Log.log.debugln("チームの処理が終了しました");
		}

		// チーム解散通知がまだ来ない or まだサブタスク処理中
		else if(!disolved) {
			member.getParameter().changeState(MemberTaskExecuteState.getState());
		}

		else {
			throw new AbnormalException("MemberTeamDissolutionConfirmationStateでこのようなパターンはありえません");
		}

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

	public static State getState() {
		return state;
	}

	public String toString() {
		return "メンバチーム解散通知確認状態";
	}

}
