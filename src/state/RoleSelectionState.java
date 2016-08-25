package state;

import java.util.HashMap;

import random.RandomKey;
import random.RandomManager;
import role.FutureRole;
import roleaction.RoleAction;
import roleaction.RoleActionManager;
import strategy.StrategyManager;
import strategy.roleselection.RoleSelectionStrategy;
import log.Log;
import constant.Constant;
import exception.AbnormalException;
import agent.Agent;

public class RoleSelectionState implements State {
	
	private static State state = new RoleSelectionState();
	private RoleSelectionStrategy roleSelectionStrategy = StrategyManager.getRoleSelectionStrategy();
	private HashMap<FutureRole, RoleAction> strategyMap = new HashMap<FutureRole, RoleAction>();
	
	private RoleSelectionState() {
		strategyMap.put(FutureRole.LEADER_FUTURE, RoleActionManager.moveToWaitingAction);
		strategyMap.put(FutureRole.MEMBER_FUTURE, RoleActionManager.participatingTeamDecisionAction);
		strategyMap.put(FutureRole.NO_ROLE_FUTURE, RoleActionManager.backToInitialStateAction);
	}

	@Override
	public void agentAction(Agent agent) {
		// リーダ時の期待報酬を計算
		double expectedLeaderReward = roleSelectionStrategy.calculateExpectedLeaderReward(agent, agent.getParameter().getMarkedTask());
		Log.log.debugln("リーダ時の期待報酬 = " + expectedLeaderReward);
		
		// メンバ時の期待報酬を計算
		double expectedMemberReward = roleSelectionStrategy.calculateExpectedMemberReward(agent, agent.getParameter().getOfferMessages());
		Log.log.debugln("メンバ時の期待報酬 = " + expectedMemberReward);
		
		// 役割を決定する
		FutureRole role = decideRole(expectedLeaderReward, expectedMemberReward);
		Log.log.debugln("役割は " + role + " に決まりました");
		
		// 役割ごとに行動する
		strategyMap.get(role).action(agent);

	}
	
	private FutureRole decideRole(double expectedLeaderReward, double expectedMemberReward) {
		// マークしたタスクはあるが、チーム履歴がない場合
		if(expectedLeaderReward == Constant.NO_PAST_TEAMS){
			// 提案メッセージもある場合
			if(expectedMemberReward > 0){
				// ランダムに役割を決める
				return RandomManager.getRandom(RandomKey.SELECT_RANDOM_5).nextInt(2) == 0 ? FutureRole.LEADER_FUTURE
						: FutureRole.MEMBER_FUTURE;
			}
			else{
				// リーダになる
				return FutureRole.LEADER_FUTURE;
			}
		}
		else if(expectedLeaderReward == 0 && expectedMemberReward == 0){
			return FutureRole.NO_ROLE_FUTURE;
		}
		else if(expectedLeaderReward >= expectedMemberReward){
			return FutureRole.LEADER_FUTURE;
		}
		else if(expectedLeaderReward < expectedMemberReward){
			return FutureRole.MEMBER_FUTURE;
		}
		else{
			throw new AbnormalException("リーダにもメンバにもなれません");
		}
	}

	public static State getState() {
		return state;
	}
	
	public String toString() {
		return "役割選択状態";
	}

}
