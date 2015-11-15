package fixed.roleaction;

import fixed.agent.FixedAgent;
import fixed.main.TeamFormationMain;
import fixed.message.FixedAnswerMessage;
import fixed.message.FixedOfferMessage;
import fixed.role.Role;
import fixed.state.SubtaskReceptionState;

public class ParticipatingTeamDecisionAction implements RoleAction {

	@Override
	public void action(FixedAgent agent) {
		// 参加するチームのリーダにはOKメッセージ、それ以外にはNGメッセージを送信する
		answerToOfferMessages(agent);
		
		// マークしようとしていたタスクがある場合は保持しないようにする
		if(agent.getParameter().getMarkedTask() != null){
			agent.getParameter().setMarkedTask(null);
		}
		
		agent.getParameter().changeState(SubtaskReceptionState.getState());
		agent.getParameter().changeRole(Role.MEMBER);
	}
	
	private void answerToOfferMessages(FixedAgent agent) {
		for(FixedOfferMessage offer : agent.getParameter().getOfferMessages()){
			if(agent.getParameter().getSelectedOfferMessage() != offer){
//				System.out.println(offer.getFrom() + " に参加NGメッセージを返信しました");
				TeamFormationMain.getPost().postAnswerMessage(offer.getFrom(), 
						new FixedAnswerMessage(agent, offer.getFrom(), false, offer.getSubtask()));
			}
			else{
//				System.out.println(offer.getFrom() + " に参加OKメッセージを返信しました");
				TeamFormationMain.getPost().postAnswerMessage(offer.getFrom(), 
						new FixedAnswerMessage(agent, offer.getFrom(), true, offer.getSubtask()));
			}
		}
	}

}
