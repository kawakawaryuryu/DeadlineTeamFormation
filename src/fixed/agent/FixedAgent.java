package fixed.agent;

import java.util.Arrays;

import fixed.constant.FixedConstant;
import fixed.main.RandomKey;
import fixed.main.RandomManager;

public class FixedAgent {
	protected int id;
	protected int[] ability;
	protected int abilitySum = 0;
	protected AgentMeasuredData measure = new AgentMeasuredData();
	protected AgentParameter parameter = new AgentParameter();
	
	double reward;
	double greedy;
	double[] trust = new double[FixedConstant.AGENT_NUM];
	double[] rewardExpectation = new double[FixedConstant.AGENT_NUM];
	
	public FixedAgent(int id) {
		this.id = id;
		ability = new int[FixedConstant.RESOURCE_NUM];
		for(int i = 0; i < ability.length; i++){
			ability[i] = RandomManager.getRandom(RandomKey.AGENT_RANDOM).nextInt(FixedConstant.AGENT_ABILITY_MAX) + FixedConstant.AGENT_ABILITY_INIT;
			abilitySum += ability[i];
		}
		
		greedy = FixedConstant.INITIAL_GREEDY;
		Arrays.fill(trust, FixedConstant.INITIAL_TRUST);
		trust[id] = 0.0;
		Arrays.fill(rewardExpectation, FixedConstant.INITIAL_EXPECTED_REWARD);
		rewardExpectation[id] = 0.0;
	}
	
	public FixedAgent(int id, int[] ability) {
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
		for(int i = 0; i < FixedConstant.RESOURCE_NUM; i++){
			str.append(ability[i] + " ");
		}
		return str.toString();
	}
	
	public void action() {
		System.out.println(this + " の行動 / " + this.parameter.state);
		parameter.getState().agentAction(this);
		System.out.println();
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
		greedy = FixedConstant.LEARN_RATE_GREEDY * value + (1.0 - FixedConstant.LEARN_RATE_GREEDY) * greedy;	//欲張り度の更新
	}
	
	public void calculateLeaderReward(boolean isok){
		reward = isok ? parameter.getMarkedTask().getTaskRequireSum() * greedy : 0.0;
	}
	
	public void feedbackTrust(FixedAgent you, boolean isok){
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
		
		trust[you.id] = FixedConstant.LEARN_RATE_TRUST * value + (1.0 - FixedConstant.LEARN_RATE_TRUST) * trust[you.id];	//提案受託期待度の更新
	}
	
	public void feedbackExpectedReward(FixedAgent you, boolean isok, int subtaskRequire, double leftReward, int leftRequireSum){
		calculateMemberReward(isok, subtaskRequire, leftReward, leftRequireSum);	//獲得報酬の計算
		int executeTime;	//実際にかかる処理時間
		if(isok){
			executeTime = parameter.getParticipatingTeam().getTeamExecuteTime();
		}
		//チーム編成に失敗した場合は1とする（0だと割り切れないため）
		else{
			executeTime = 1;
		}
		rewardExpectation[you.id] = FixedConstant.LEARN_RATE_REWARD * (reward / (double)executeTime) + (1.0 - FixedConstant.LEARN_RATE_REWARD) * rewardExpectation[you.id];	//報酬期待度の更新
	}
	
	public void calculateMemberReward(boolean isok, int subtaskRequire, double leftReward, int leftRequireSum){
		reward = isok ? leftReward * ((double)subtaskRequire / (double)leftRequireSum) : 0.0;	//獲得報酬
	}
	
	public double getGreedy() {
		return greedy;
	}
	
	public double getTrust(FixedAgent you) {
		return trust[you.id];
	}
	
	public double[] getTrust() {
		return trust;
	}
	
	public double getRewardExpectation(FixedAgent you) {
		return rewardExpectation[you.id];
	}
}
