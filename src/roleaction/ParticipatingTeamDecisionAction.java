package roleaction;

import role.Role;
import state.MemberWaitingState;
import state.StateManager;
import state.SubtaskReceptionState;
import task.Failure;
import library.MessageLibrary;
import log.Log;
import main.teamformation.TeamFormationInstances;
import message.AnswerMessage;
import message.OfferMessage;
import agent.Agent;

public class ParticipatingTeamDecisionAction implements RoleAction {

	@Override
	public void action(Agent agent) {
		// 参加するチームのリーダにはOKメッセージ、それ以外にはNGメッセージを送信する
		answerToOfferMessages(agent);
		
		// マークしていたタスクがある場合はマークを外す
		if(agent.getParameter().getMarkedTask() != null){
			agent.getParameter().getMarkedTask().markingTask(false, Failure.DECIDE_MEMBER_FAILURE);
		}
		
		StateManager.changeStateConsideringDelay(agent, SubtaskReceptionState.getState(), MemberWaitingState.getState());
		agent.getParameter().changeRole(Role.MEMBER);
	}
	
	private void answerToOfferMessages(Agent agent) {
		for(OfferMessage offer : agent.getParameter().getOfferMessages()){
			int delayTime = MessageLibrary.getMessageTime(agent, offer.getFrom());
			if(agent.getParameter().getSelectedOfferMessage() != offer){
				Log.log.debugln(offer.getFrom() + " に参加NGメッセージを返信しました");
				TeamFormationInstances.getInstance().getPost().postAnswerMessage(offer.getFrom(),
						new AnswerMessage(agent, offer.getFrom(), delayTime, false, offer.getSubtask()));
			}
			else{
				Log.log.debugln(offer.getFrom() + " に参加OKメッセージを返信しました");
				TeamFormationInstances.getInstance().getPost().postAnswerMessage(offer.getFrom(),
						new AnswerMessage(agent, offer.getFrom(), delayTime, true, offer.getSubtask()));
			}
		}
	}

}
