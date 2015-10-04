package fixed.state;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.main.RandomKey;
import fixed.main.RandomManager;
import fixed.role.FutureRole;
import fixed.roleaction.BackToInitialStateAction;
import fixed.roleaction.ParticipatingTeamDecisionAction;
import fixed.roleaction.RoleAction;
import fixed.roleaction.TentativeMemberSelectionAction;
import fixed.strategy.StrategyManager;
import fixed.strategy.roleselection.FixedRoleSelectionStrategy;

import java.util.HashMap;

public class RoleSelectionState implements FixedState {
	
	private static FixedState state = new RoleSelectionState();
	private FixedRoleSelectionStrategy roleSelectionStrategy = StrategyManager.getRoleSelectionStrategy();
	private HashMap<FutureRole, RoleAction> strategyMap = new HashMap<FutureRole, RoleAction>();
	
	private RoleSelectionState() {
		strategyMap.put(FutureRole.LEADER_FUTURE, new TentativeMemberSelectionAction());
		strategyMap.put(FutureRole.MEMBER_FUTURE, new ParticipatingTeamDecisionAction());
		strategyMap.put(FutureRole.NO_ROLE_FUTURE, new BackToInitialStateAction());
	}

	@Override
	public void agentAction(FixedAgent agent) {
		// リーダ時の期待報酬を計算
		double expectedLeaderReward = roleSelectionStrategy.calculateExpectedLeaderReward(agent, agent.getParameter().getMarkedTask());
		System.out.println("リーダ時の期待報酬 = " + expectedLeaderReward);
		
		// メンバ時の期待報酬を計算
		double expectedMemberReward = roleSelectionStrategy.calculateExpectedMemberReward(agent, agent.getParameter().getOfferMessages());
		System.out.println("メンバ時の期待報酬 = " + expectedMemberReward);
		
		// 役割を決定する
		FutureRole role = decideRole(expectedLeaderReward, expectedMemberReward);
		System.out.println("役割は " + role + " に決まりました");
		
		// 役割ごとに行動する
		strategyMap.get(role).action(agent);

	}
	
	private FutureRole decideRole(double expectedLeaderReward, double expectedMemberReward) {
		// マークしたタスクはあるが、チーム履歴がない場合
		if(expectedLeaderReward == FixedConstant.NO_PAST_TEAMS){
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
			System.err.println("リーダにもメンバにもなれません");
			System.exit(-1);
			return null;
		}
	}

	public static FixedState getState() {
		return state;
	}
	
	public String toString() {
		return "役割選択状態";
	}

}
