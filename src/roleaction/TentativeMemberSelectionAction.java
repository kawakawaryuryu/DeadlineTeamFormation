package roleaction;

import role.Role;
import state.SubtaskAllocationState;
import state.TaskSelectionState;
import strategy.StrategyManager;
import strategy.memberselection.TentativeMemberSelectionStrategy;
import task.Failure;
import team.FixedTeam;
import main.TeamFormationMain;
import message.FixedAnswerMessage;
import message.FixedOfferMessage;
import agent.FixedAgent;

public class TentativeMemberSelectionAction implements RoleAction {
	private TentativeMemberSelectionStrategy strategy = StrategyManager.getMemberSelectionStrategy();

	@Override
	public void action(FixedAgent agent) {
		// 提案メッセージの全送信者に参加拒否メッセージを送る
		refuseOfferMessages(agent);
		
		// メンバ候補を選んで提案メッセージを送る
		boolean isCandidates = strategy.searchTentativeMembers(agent, agent.getParameter().getMarkedTask());	
		
		// メンバ候補を探せた場合
		if(isCandidates){
//			System.out.println("メンバ候補探しに成功しました");
			actionInSearchingSuccessCase(agent);
		}
		// メンバ候補を探せなかった場合
		else{
//			System.out.println("メンバ候補探しに失敗しました");
			actionInSearchingFailureCase(agent);
		}

	}
	
	private void refuseOfferMessages(FixedAgent agent) {
		for(FixedOfferMessage offer : agent.getParameter().getOfferMessages()){
//			System.out.println(offer.getFrom() + "からのメッセージを断ります");
			TeamFormationMain.getPost().postAnswerMessage(offer.getFrom(), 
					new FixedAnswerMessage(agent, offer.getFrom(), false, offer.getSubtask()));
		}
	}
	
	private void actionInSearchingSuccessCase(FixedAgent agent) {
		agent.getParameter().setParticipatingTeam(new FixedTeam(agent));
		agent.getParameter().changeState(SubtaskAllocationState.getState());
		agent.getParameter().changeRole(Role.LEADER);
	}
	
	private void actionInSearchingFailureCase(FixedAgent agent) {
		agent.getParameter().getMarkedTask().markingTask(false, Failure.ESTIMATION_FAILURE);
		agent.getParameter().getMarkedTask().clear();
		TeamFormationMain.getMeasure().countGiveUpTeamFormationNum();
		agent.getParameter().changeState(TaskSelectionState.getState());
	}
	
	

}
