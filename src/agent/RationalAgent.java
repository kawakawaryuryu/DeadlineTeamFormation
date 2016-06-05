package agent;

import constant.Constant;

public class RationalAgent extends Agent {

	public RationalAgent(int id) {
		super(id);
	}

	public RationalAgent(int id, int[] ability) {
		super(id, ability);
	}
	

	@Override
	public String type() {
		return "Rational";
	}

	@Override
	public void calculateLeaderReward(boolean isok) {
		reward = isok ? parameter.getMarkedTask().getTaskRequireSum() * greedy : 0.0;
	}

	@Override
	public void calculateMemberReward(boolean isok, int subtaskRequire,
			double leftReward, int leftRequireSum) {
		reward = isok ? leftReward * ((double)subtaskRequire / (double)leftRequireSum) : 0.0;	//獲得報酬
	}

	@Override
	public void feedbackGreedy(boolean isok) {
		double value = isok ? 1.0 : 0.0;
		calculateLeaderReward(isok);	//獲得報酬の計算
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
	public void feedbackExpectedReward(Agent you, boolean isok,
			int subtaskRequire, double leftReward, int leftRequireSum) {
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


}
