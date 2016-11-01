package agent;

import java.util.Arrays;

import library.AgentTaskLibrary;
import agent.paramter.AbstractAgentParameter;
import task.Task;
import team.Team;
import constant.Constant;

/**
 * チーム固定化エージェント
 */
public class StructuredAgent extends Agent {

	double[] trustToLeader = new double[Constant.AGENT_NUM];

	public StructuredAgent(int id, AbstractAgentParameter parameter) {
		super(id, parameter);
		Arrays.fill(trustToLeader, Constant.INITIAL_TRUST_TO_LEADER);
		trustToLeader[id] = 0.0;
	}

	public StructuredAgent(int id, int[] ability, AbstractAgentParameter parameter) {
		super(id, ability, parameter);
		Arrays.fill(trustToLeader, Constant.INITIAL_TRUST_TO_LEADER);
		trustToLeader[id] = 0.0;
	}

	@Override
	public String type() {
		return "Structured";
	}

	@Override
	public double calculateLeaderReward(boolean isok, Task executedTask) {
		reward = isok ? executedTask.getTaskRequireSum() * greedy : 0.0;
		return reward;
	}

	@Override
	public double calculateMemberReward(boolean isok, int subtaskRequire,
			double leftReward, int leftRequireSum) {
		reward = isok ? leftReward * ((double)subtaskRequire / (double)leftRequireSum) : 0.0;	//獲得報酬
		return reward;
	}

	@Override
	public void feedbackGreedy(boolean isok) {
		double value = isok ? 1.0 : 0.0;
		greedy = Constant.LEARN_RATE_GREEDY * value + (1.0 - Constant.LEARN_RATE_GREEDY) * greedy;	//欲張り度の更新
	}

	@Override
	public void feedbackTrustToMember(Agent you, boolean isok) {
		double value;
		if(isok){
			// 自分の実行時間より長い時間かかる場合
			if(this.parameter.getExecuteTime() < you.parameter.getExecuteTime()){
				value = (double)this.parameter.getExecuteTime() / (double)you.parameter.getExecuteTime();
			}
			else{
				value = 1.0;
			}
		}
		else{
			value = 0.0;
		}
		
		trustToMember[you.id] = Constant.LEARN_RATE_TRUST_TO_MEMBER * value + (1.0 - Constant.LEARN_RATE_TRUST_TO_MEMBER) * trustToMember[you.id];	//提案受託期待度の更新
	}

	@Override
	// TODO rewrdは引数で与えるようにする
	public void feedbackExpectedReward(Agent you, boolean isok,
			int subtaskRequire, double leftReward, int leftRequireSum) {
		rewardExpectation[you.id] = Constant.LEARN_RATE_REWARD * AgentTaskLibrary.getRewardPerTurn(parameter, isok, reward)
				+ (1.0 - Constant.LEARN_RATE_REWARD) * rewardExpectation[you.id];	//報酬期待度の更新
	}

	public void feedbackTrustToLeader(Agent you, Team team, boolean isok) {
		double value;
		if(isok){
			// 自分の実行時間より長い時間かかる場合
			if(this.parameter.getExecuteTime() < team.getTeamExecuteTime()){
				value = (double)this.parameter.getExecuteTime() / (double)team.getTeamExecuteTime();
			}
			else{
				value = 1.0;
			}
		}
		else{
			value = 0.0;
		}
		
		//リーダに対する信頼度の更新
		trustToLeader[you.id] = Constant.LEARN_RATE_TRUST_TO_LEADER * value + (1.0 - Constant.LEARN_RATE_TRUST_TO_LEADER) * trustToLeader[you.id];
	
		// 信頼エージェントの更新
		if (trustToLeader[you.id] > Constant.TRUST_LEADER_THREASHOLD && !this.parameter.containsTrustLeader(you)) {
			this.parameter.setTrustLeaders(you);
		}
		else if (trustToLeader[you.id] <= Constant.TRUST_LEADER_THREASHOLD && this.parameter.containsTrustLeader(you)) {
			this.parameter.removeTrustLeader(you);
		}
	}

	public void decreaseTrustToLeader(Agent you) {
		trustToLeader[you.id] -= Constant.TRUST_DECREMENT_VALUE;
		if (trustToLeader[you.id] < 0) trustToLeader[you.id] = 0.0;
	}

	public double getTrustToLeader(Agent you) {
		return trustToLeader[you.id];
	}

	public double[] getTrustToLeader() {
		return trustToLeader;
	}

	@Override
	public void feedbackLeaderRewardExpectation(double reward, boolean isok) {
		leaderRewardExpectation = Constant.LEARN_RATE_LEADER_REWARD_EXPECTATION * AgentTaskLibrary.getRewardPerTurn(parameter, isok, reward)
				+ (1.0 - Constant.LEARN_RATE_LEADER_REWARD_EXPECTATION) * leaderRewardExpectation;
	}

	@Override
	public void feedbackMemberRewardExpectation(double reward, boolean isok) {
		memberRewardExpectation = Constant.LEARN_RATE_MEMBER_REWARD_EXPECTATION * AgentTaskLibrary.getRewardPerTurn(parameter, isok, reward)
				+ (1.0 - Constant.LEARN_RATE_MEMBER_REWARD_EXPECTATION) * memberRewardExpectation;
	}

}
