package fixed.roleaction;

import fixed.agent.FixedAgent;
import fixed.main.TeamFormationMain;
import fixed.message.FixedAnswerMessage;
import fixed.message.FixedOfferMessage;
import fixed.role.Role;
import fixed.state.SubtaskAllocationState;
import fixed.state.TaskSelectionState;
import fixed.strategy.StrategyManager;
import fixed.strategy.memberselection.TentativeMemberSelectionStrategy;
import fixed.team.FixedTeam;

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
		agent.getParameter().getMarkedTask().clear();
		agent.getParameter().getMarkedTask().markingTask(false);
		agent.getParameter().setMarkedTask(null);
		TeamFormationMain.getMeasure().countGiveUpTeamFormationNum();
		agent.getParameter().changeState(TaskSelectionState.getState());
	}
	
	

}
