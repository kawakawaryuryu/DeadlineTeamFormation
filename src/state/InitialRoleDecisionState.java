package state;

import log.Log;
import main.teamformation.TeamFormationInstances;
import constant.Constant;
import exception.AbnormalException;
import random.RandomKey;
import random.RandomManager;
import role.FutureRole;
import agent.Agent;

public class InitialRoleDecisionState implements State {

	private static State state = new InitialRoleDecisionState();

	@Override
	public void agentAction(Agent agent) {

		// 確率を算出
		double probability = RandomManager.getRandom(RandomKey.EPSILON_ROLE_DECISION_RANDOM).nextDouble();

		FutureRole role;

		Log.log.debugln("リーダ時報酬期待度 = " + agent.getLeaderRewardExpectation());
		Log.log.debugln("メンバ時報酬期待度 = " + agent.getMemberRewardExpectation());

		// 確率がε以下もしくはリーダ、メンバ時報酬期待度が等しい場合
		if (probability <= Constant.EPSILON_ROLE || agent.getLeaderRewardExpectation() == agent.getMemberRewardExpectation()) {
			// ランダムに役割を決定
			role = RandomManager.getRandom(RandomKey.ROLE_DECISION_RANDOM).nextBoolean()
					? FutureRole.LEADER_FUTURE : FutureRole.MEMBER_FUTURE;
		}
		else if (agent.getLeaderRewardExpectation() > agent.getMemberRewardExpectation()) {
			role = FutureRole.LEADER_FUTURE;
		}
		else if (agent.getLeaderRewardExpectation() < agent.getMemberRewardExpectation()) {
			role = FutureRole.MEMBER_FUTURE;
		}
		else {
			throw new AbnormalException("役割決定においてこのような場合は存在しません");
		}

		Log.log.debugln("役割は" + role + "に決定しました");

		// 次の状態に移行
		if (role == FutureRole.LEADER_FUTURE) {
			agent.getParameter().changeState(TaskSelectionState.getState());
			TeamFormationInstances.getInstance().getParameter().addAgentToAgentsMap(TaskSelectionState.getState(), agent);
		}
		else if (role == FutureRole.MEMBER_FUTURE) {
			agent.getParameter().changeState(InitialMemberState.getState());
			TeamFormationInstances.getInstance().getParameter().addAgentToAgentsMap(InitialMemberState.getState(), agent);
		}
		else {
			throw new AbnormalException("このような役割の場合はありえません");
		}
	}

	public static State getState() {
		return state;
	}

	public String toString() {
		return "初期役割決定状態";
	}

}
