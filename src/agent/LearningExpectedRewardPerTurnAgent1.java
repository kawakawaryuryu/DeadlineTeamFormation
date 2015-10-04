package agent;

import main.Constant;

/**
 * 報酬期待度は1tickごとにもらえる期待報酬のうち1tickごとにもらえる実際の報酬を学習
 * 拘束時間の分で提案受託期待度を学習
 */
public class LearningExpectedRewardPerTurnAgent1 extends Agent {
	
	/**
	 * コンストラクタ
	 * @param id
	 * @param ability
	 */
	public LearningExpectedRewardPerTurnAgent1(int id, int[] ability) {
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
	 * (実際に受け取る報酬 / 実際に拘束される時間) / (期待報酬 / 期待拘束時間)
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
		expectedReward[you.id] = Constant.LEARN_RATE_REWARD * ((reward / (double)executeTime) / ((double)selectedOfferMessage.getSubTask().getRequireSum() / (double)calculateExpectedExecuteTime())) + (1.0 - Constant.LEARN_RATE_REWARD) * expectedReward[you.id];	//報酬期待度の更新
//		System.out.println(" / （後） = " + expectedReward[you.id]);
	}
	
	/**
	 * 期待拘束時間を計算する
	 * @return
	 */
	public int calculateExpectedExecuteTime(){
		int[] time = new int[Constant.RESOURCE_NUM];
		int executeTime = 0;
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			/* タスク処理にかかる時間の計算 */
			time[i] = (int)Math.ceil((double)selectedOfferMessage.getSubTask().getRequire()[i] / (double)ability[i]);
			
			if(executeTime < time[i]){
				executeTime = time[i];
			}
		}
		return executeTime;
	}
	
	/**
	 * エージェントのタイプを返す
	 * @return
	 */
	@Override
	public String getAgentType(){
		return "単位時間の期待報酬に対する単位時間の獲得報酬を学習するエージェント";
	}
}
