package roleaction;

import role.Role;
import state.SubtaskReceptionState;
import task.Failure;
import main.TeamFormationMain;
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
		
		agent.getParameter().changeState(SubtaskReceptionState.getState());
		agent.getParameter().changeRole(Role.MEMBER);
	}
	
	private void answerToOfferMessages(Agent agent) {
		for(OfferMessage offer : agent.getParameter().getOfferMessages()){
			if(agent.getParameter().getSelectedOfferMessage() != offer){
//				System.out.println(offer.getFrom() + " に参加NGメッセージを返信しました");
				TeamFormationMain.getPost().postAnswerMessage(offer.getFrom(), 
						new AnswerMessage(agent, offer.getFrom(), false, offer.getSubtask()));
			}
			else{
//				System.out.println(offer.getFrom() + " に参加OKメッセージを返信しました");
				TeamFormationMain.getPost().postAnswerMessage(offer.getFrom(), 
						new AnswerMessage(agent, offer.getFrom(), true, offer.getSubtask()));
			}
		}
	}

}
