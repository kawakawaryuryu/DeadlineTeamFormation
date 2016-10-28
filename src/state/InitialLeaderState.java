package state;

import exception.AbnormalException;
import roleaction.RoleAction;
import roleaction.RoleActionManager;
import strategy.StrategyManager;
import strategy.roleselection.RoleSelectionStrategy;
import agent.Agent;

public class InitialLeaderState implements State {

	private static State state = new InitialLeaderState();

	private RoleSelectionStrategy roleSelectionStrategy = StrategyManager.getRoleSelectionStrategy();
	private RoleAction moveToWaitingAction = RoleActionManager.moveToWaitingAction;
	private RoleAction backToInitialAction = RoleActionManager.backToInitialStateAction;

	@Override
	public void agentAction(Agent agent) {
		double leaderReward = roleSelectionStrategy.calculateExpectedLeaderReward(agent, agent.getParameter().getMarkedTask());
		if (leaderReward != 0 && agent.getParameter().getMarkedTask() != null) {
			// タスクコピー待ち or 仮メンバを選択
			moveToWaitingAction.action(agent);
		}
		else if (leaderReward == 0 && agent.getParameter().getMarkedTask() == null) {
			// タスクをマークできなかった場合は初期状態に移行
			backToInitialAction.action(agent);
		}
		else {
			throw new AbnormalException("初期リーダ状態においてこのような場合は存在しません");
		}
	}

	public static State getState() {
		return state;
	}

	public String toString() {
		return "初期リーダ状態(リーダ決定後仮メンバ選択を行う)";
	}

}
