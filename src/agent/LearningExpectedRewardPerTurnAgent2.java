package agent;

import main.Constant;

/**
 * 報酬期待度を
 * 1tickごとに獲得した報酬で学習
 * (獲得報酬 / チーム処理時間)で学習
 */
public class LearningExpectedRewardPerTurnAgent2 extends Agent {

	/**
	 * コンストラクタ
	 * @param id
	 * @param ability
	 */
	public LearningExpectedRewardPerTurnAgent2(int id, int[] ability) {
		super(id, ability);
	}
	
	/**
	 * 提案受託期待度の更新
	 * @param you
	 * @param isok
	 */
	public void feedbackTrust(Agent you, boolean isok){
		double value;
		if(isok){
			/* 自分の実行時間より長い時間かかる場合 */
			if(this.getExecuteTime() < you.getExecuteTime()){
				value = (double)this.getExecuteTime() / (double)you.getExecuteTime();
			}
			else{
				value = 1.0;
			}
		}
		else{
			value = 0.0;
		}
		
//		System.out.print(this + " の " + you + "　に対する提案受託期待度：（前） = " + trust[you.id]);
		trust[you.id] = Constant.LEARN_RATE_TRUST * value + (1.0 - Constant.LEARN_RATE_TRUST) * trust[you.id];	//提案受託期待度の更新
//		System.out.println(" / （後） = " + trust[you.id]);	
	}
	
	/**
	 * 報酬期待度の更新
	 * (実際に受け取る報酬 / 実際に拘束される時間)
	 * @param you
	 * @param isok
	 * @param subtaskRequire
	 * @param leftReward
	 * @param leftRequireSum
	 */
	public void feedbackExpectedReward(Agent you, boolean isok, int subtaskRequire, double leftReward, int leftRequireSum){
		calculateMemberReward(isok, subtaskRequire, leftReward, leftRequireSum);	//獲得報酬の計算
		int executeTime;	//実際にかかる処理時間
		//チーム編成に成功した場合
		if(isok){
			executeTime = getTeamInfo().getExecuteTime();
		}
		//チーム編成に失敗した場合は1とする（0だと割り切れないため）
		else{
			executeTime = 1;
		}
//		System.out.println(this + " の獲得報酬 = " + reward);
//		System.out.print(this + " の " + you + " に対する報酬期待度：（前） = " + expectedReward[you.id]);
		expectedReward[you.id] = Constant.LEARN_RATE_REWARD * (reward / (double)executeTime) + (1.0 - Constant.LEARN_RATE_REWARD) * expectedReward[you.id];	//報酬期待度の更新
//		System.out.println(" / （後） = " + expectedReward[you.id]);
	}
	
	
	/**
	 * エージェントのタイプを返す
	 * @return
	 */
	@Override
	public String getAgentType(){
		return "1tickごとに獲得した報酬で学習";
	}

}
