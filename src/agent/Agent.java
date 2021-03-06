package agent;

import java.util.Arrays;

import agent.paramter.AbstractAgentParameter;
import log.Log;
import random.RandomKey;
import random.RandomManager;
import task.Task;
import constant.Constant;

public abstract class Agent {

	protected int id;
	protected int[] ability;
	protected int abilitySum = 0;
	protected AgentMeasuredData measure = new AgentMeasuredData();
	protected AbstractAgentParameter parameter;

	// TODO rewardはエージェントに持たせる必要なし
	double reward;
	double greedy;
	double[] trustToMember = new double[Constant.AGENT_NUM];
	double[] rewardExpectation = new double[Constant.AGENT_NUM];
	double leaderRewardExpectation;
	double memberRewardExpectation;


	public Agent(int id, AbstractAgentParameter parameter) {
		this.id = id;
		this.parameter = parameter;
		ability = new int[Constant.RESOURCE_NUM];
		for(int i = 0; i < ability.length; i++){
			ability[i] = RandomManager.getRandom(RandomKey.AGENT_RANDOM).nextInt(Constant.AGENT_ABILITY_MAX) + Constant.AGENT_ABILITY_INIT;
			abilitySum += ability[i];
		}

		greedy = Constant.INITIAL_GREEDY;
		Arrays.fill(trustToMember, Constant.INITIAL_TRUST_TO_MEMBER);
		trustToMember[id] = 0.0;
		Arrays.fill(rewardExpectation, Constant.INITIAL_EXPECTED_REWARD);
		rewardExpectation[id] = 0.0;
		leaderRewardExpectation = Constant.INITIAL_LEADER_REWARD_EXPECTATION;
		memberRewardExpectation = Constant.INITIAL_MEMBER_REWARD_EXPECTATION;
	}

	public Agent(int id, int[] ability, AbstractAgentParameter parameter) {
		this(id, parameter);
		abilitySum = 0;
		for(int i = 0; i < this.ability.length; i++){
			this.ability[i] = ability[i];
			abilitySum += this.ability[i];
		}

	}

	public int getId() {
		return id;
	}

	public int getAbility(int i) {
		return ability[i];
	}

	public int getAbilitySum() {
		return abilitySum;
	}

	public AbstractAgentParameter getParameter() {
		return parameter;
	}

	public AgentMeasuredData getMeasure() {
		return measure;
	}

	public void action() {
		Log.log.debugln(this + " の行動 / " + this.parameter.getState());
		parameter.getState().agentAction(this);
		Log.log.debugln();
	}

	public double getGreedy() {
		return greedy;
	}

	public void decreaseTrustToMember(Agent you) {
		trustToMember[you.id] -= Constant.TRUST_DECREMENT_VALUE;
		if (trustToMember[you.id] < 0) trustToMember[you.id] = 0.0;
	}

	public double getTrustToMember(Agent you) {
		return trustToMember[you.id];
	}

	public double[] getTrustToMember() {
		return trustToMember;
	}

	public double getRewardExpectation(Agent you) {
		return rewardExpectation[you.id];
	}

	public double getLeaderRewardExpectation() {
		return leaderRewardExpectation;
	}

	public double getMemberRewardExpectation() {
		return memberRewardExpectation;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("id=" + id + "/ability=");
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			str.append(ability[i] + " ");
		}
		return str.toString();
	}


	//------------------------
	// 抽象メソッド
	//------------------------

	public abstract String type();

	public abstract double calculateLeaderReward(boolean isok, Task executedTask);

	public abstract double calculateMemberReward(boolean isok, int subtaskRequire, double leftReward, int leftRequireSum);

	public abstract void feedbackGreedy(boolean isok);

	public abstract void feedbackTrustToMember(Agent you, boolean isok);

	public abstract void feedbackExpectedReward(Agent you, boolean isok, int subtaskRequire, double leftReward, int leftRequireSum);

	public abstract void feedbackLeaderRewardExpectation(double reward, boolean isok);

	public abstract void feedbackMemberRewardExpectation(double reward, boolean isok);
}
