package state;

import exception.AbnormalException;
import roleaction.RoleAction;
import roleaction.RoleActionManager;
import strategy.StrategyManager;
import strategy.roleselection.RoleSelectionStrategy;
import agent.Agent;

public class InitialMemberState implements State {

	private static State state = new InitialMemberState();

	private RoleSelectionStrategy roleSelectionStrategy = StrategyManager.getRoleSelectionStrategy();
	private RoleAction participatingTeamAction = RoleActionManager.participatingTeamDecisionAction;
	private RoleAction backToInitialAction = RoleActionManager.backToInitialStateAction;

	@Override
	public void agentAction(Agent agent) {
		double memberReward = roleSelectionStrategy.calculateExpectedMemberReward(agent, agent.getParameter().getOfferMessages());
		if (memberReward > 0) {
			// メッセージ選択を行う
			participatingTeamAction.action(agent);
		}
		else if (memberReward == 0) {
			// メッセージがない場合は初期状態に戻る
			backToInitialAction.action(agent);
		}
		else {
			throw new AbnormalException("初期メンバ状態においてこのような場合は存在しません");
		}
	}

	public static State getState() {
		return state;
	}

	public String toString() {
		return "初期メンバ状態 (メンバ決定後にメッセージ選択を行う)";
	}

}
