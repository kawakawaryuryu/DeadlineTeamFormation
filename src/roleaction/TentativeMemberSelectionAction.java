package roleaction;

import config.Configuration;
import role.Role;
import state.LeaderWaitingState;
import state.StateManager;
import state.SubtaskAllocationState;
import strategy.StrategyManager;
import strategy.memberselection.TentativeMemberSelectionStrategy;
import task.Failure;
import team.Team;
import library.MessageLibrary;
import log.Log;
import main.teamformation.TeamFormationInstances;
import message.AnswerMessage;
import message.OfferMessage;
import action.ActionManager;
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
			int delayTime = MessageLibrary.getMessageTime(agent, offer.getFrom());
			Log.log.debugln(offer.getFrom() + "からのメッセージを断ります");
			TeamFormationInstances.getInstance().getPost().postAnswerMessage(offer.getFrom(),
					new AnswerMessage(agent, offer.getFrom(), delayTime, false, offer.getSubtask()));
		}
	}
	
	private void actionInSearchingSuccessCase(Agent agent) {
		agent.getParameter().setParticipatingTeam(new Team(agent));
		StateManager.changeStateConsideringDelay(agent, SubtaskAllocationState.getState(), LeaderWaitingState.getState());
		agent.getParameter().changeRole(Role.LEADER);
	}
	
	private void actionInSearchingFailureCase(Agent agent) {
		agent.getParameter().getMarkedTask().countFailure(Failure.ESTIMATION_FAILURE);
		Configuration.greedyPenaltyStrategy.decreaseGreedy(agent);
		agent.getParameter().getMarkedTask().clear();
		TeamFormationInstances.getInstance().getMeasure().countGiveUpTeamFormationNum();
		ActionManager.toTaskReturnedWaitingStateAction.action(agent);
	}
	
	

}
