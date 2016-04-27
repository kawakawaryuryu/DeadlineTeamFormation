package roleaction;

import role.Role;
import state.SubtaskAllocationState;
import state.TaskSelectionState;
import strategy.StrategyManager;
import strategy.memberselection.TentativeMemberSelectionStrategy;
import task.Failure;
import team.Team;
import log.Log;
import main.TeamFormationMain;
import message.AnswerMessage;
import message.OfferMessage;
import agent.Agent;

public class TentativeMemberSelectionAction implements RoleAction {
	private TentativeMemberSelectionStrategy strategy = StrategyManager.getMemberSelectionStrategy();

	@Override
	public void action(Agent agent) {
		// 提案メッセージの全送信者に参加拒否メッセージを送る
		refuseOfferMessages(agent);
		
		// メンバ候補を選んで提案メッセージを送る
		boolean isCandidates = strategy.searchTentativeMembers(agent, agent.getParameter().getMarkedTask());	
		
		// メンバ候補を探せた場合
		if(isCandidates){
			Log.log.debugln("メンバ候補探しに成功しました");
			actionInSearchingSuccessCase(agent);
		}
		// メンバ候補を探せなかった場合
		else{
			Log.log.debugln("メンバ候補探しに失敗しました");
			actionInSearchingFailureCase(agent);
		}

	}
	
	private void refuseOfferMessages(Agent agent) {
		for(OfferMessage offer : agent.getParameter().getOfferMessages()){
			Log.log.debugln(offer.getFrom() + "からのメッセージを断ります");
			TeamFormationMain.getPost().postAnswerMessage(offer.getFrom(), 
					new AnswerMessage(agent, offer.getFrom(), false, offer.getSubtask()));
		}
	}
	
	private void actionInSearchingSuccessCase(Agent agent) {
		agent.getParameter().setParticipatingTeam(new Team(agent));
		agent.getParameter().changeState(SubtaskAllocationState.getState());
		agent.getParameter().changeRole(Role.LEADER);
	}
	
	private void actionInSearchingFailureCase(Agent agent) {
		agent.getParameter().getMarkedTask().markingTask(false, Failure.ESTIMATION_FAILURE);
		agent.getParameter().getMarkedTask().clear();
		TeamFormationMain.getMeasure().countGiveUpTeamFormationNum();
		agent.getParameter().changeState(TaskSelectionState.getState());
	}
	
	

}
