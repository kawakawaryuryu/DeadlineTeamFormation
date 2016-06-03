package agent;

import java.util.Arrays;

import team.Team;
import random.RandomKey;
import random.RandomManager;
import log.Log;
import constant.Constant;

public class ConcreteAgent {
	protected int id;
	protected int[] ability;
	protected int abilitySum = 0;
	protected AgentMeasuredData measure = new AgentMeasuredData();
	protected AgentParameter parameter = new AgentParameter();
	
	double reward;
	double greedy;
	double[] trustToMember = new double[Constant.AGENT_NUM];
	double[] rewardExpectation = new double[Constant.AGENT_NUM];
	double[] trustToLeader = new double[Constant.AGENT_NUM];
	
	public ConcreteAgent(int id) {
		this.id = id;
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
		Arrays.fill(trustToLeader, Constant.INITIAL_TRUST_TO_LEADER);
		trustToLeader[id] = 0.0;
	}
	
	public ConcreteAgent(int id, int[] ability) {
		this(id);
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
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("id = " + id + " / ability = ");
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			str.append(ability[i] + " ");
		}
		return str.toString();
	}
	
	public void action() {
		Log.log.debugln(this + " の行動 / " + this.parameter.state);
		parameter.getState().agentAction(this);
		Log.log.debugln();
	}
	
	public AgentParameter getParameter() {
		return parameter;
	}
	
	public AgentMeasuredData getMeasure() {
		return measure;
	}
	
	public void feedbackGreedy(boolean isok){
		double value = isok ? 1.0 : 0.0;
		calculateLeaderReward(isok);	//獲得報酬の計算
		greedy = Constant.LEARN_RATE_GREEDY * value + (1.0 - Constant.LEARN_RATE_GREEDY) * greedy;	//欲張り度の更新
	}
	
	public void calculateLeaderReward(boolean isok){
		reward = isok ? parameter.getMarkedTask().getTaskRequireSum() * greedy : 0.0;
	}
	
	public void feedbackTrustToMember(ConcreteAgent you, boolean isok){
		double value;
		if(isok){
			/* 自分の実行時間より長い時間かかる場合 */
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
	
	public void feedbackExpectedReward(ConcreteAgent you, boolean isok, int subtaskRequire, double leftReward, int leftRequireSum){
		calculateMemberReward(isok, subtaskRequire, leftReward, leftRequireSum);	//獲得報酬の計算
		int executeTime;	//実際にかかる処理時間
		if(isok){
			executeTime = parameter.getParticipatingTeam().getTeamExecuteTime();
		}
		//チーム編成に失敗した場合は1とする（0だと割り切れないため）
		else{
			executeTime = 1;
		}
		rewardExpectation[you.id] = Constant.LEARN_RATE_REWARD * (reward / (double)executeTime) + (1.0 - Constant.LEARN_RATE_REWARD) * rewardExpectation[you.id];	//報酬期待度の更新
	}
	
	public void feedbackTrustToLeader(ConcreteAgent you, Team team, boolean isok) {
		double value;
		if(isok){
			/* 自分の実行時間より長い時間かかる場合 */
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
		if (trustToLeader[you.id] > Constant.TRUST_LEADER_THREASHOLD && !this.parameter.trustLeaders.contains(you)) {
			this.parameter.setTrustLeaders(you);
		}
		else if (trustToLeader[you.id] <= Constant.TRUST_LEADER_THREASHOLD && this.parameter.trustLeaders.contains(you)) {
			this.parameter.removeTrustLeader(you);
		}
	}
	
	public void calculateMemberReward(boolean isok, int subtaskRequire, double leftReward, int leftRequireSum){
		reward = isok ? leftReward * ((double)subtaskRequire / (double)leftRequireSum) : 0.0;	//獲得報酬
	}
	
	public double getGreedy() {
		return greedy;
	}
	
	public double getTrustToMember(ConcreteAgent you) {
		return trustToMember[you.id];
	}
	
	public double[] getTrustToMember() {
		return trustToMember;
	}
	
	public double getRewardExpectation(ConcreteAgent you) {
		return rewardExpectation[you.id];
	}
	
	public double getTrustToLeader(ConcreteAgent you) {
		return trustToLeader[you.id];
	}
	
	public double[] getTrustToLeader() {
		return trustToLeader;
	}
}
