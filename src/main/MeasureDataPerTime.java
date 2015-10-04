package main;

/**
 * タスク処理リソース量の平均を計測する（保持する）ためのクラス
 * 1回の実験ごとの処理リソース量などを保持する
 * tターンごとに計測する結果を保持
 */
public class MeasureDataPerTime {
	
	/* measureRequireSumの結果を保持する配列 */
	static double[] successTaskRequireArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] failureTaskRequireArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] failureTaskNumArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] successTeamingCountArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] failureTeamingCountArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] giveUpTeamingCountArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] teamingCountArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] constraintTimeInATeamArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] constraintTimePerAgentInATeamArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] constraintTimePerAgentArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] successTeamingCountAgainAllocationEndArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	
	/**
	 * タスク処理リソース量を計測する
	 * @param require_num
	 * @param successTaskRequire
	 * @param failureTaskRequire
	 * @param failureTaskNum
	 * @param successTeamingCount
	 * @param failureTeamingCount
	 * @param giveUpTeamingCount 
	 * @param teamingCount
	 * @param constraintTimeInATeam
	 * @param constraintTimePerAgentInATeam
	 * @param constraintTimePerAgent
	 * @param succuessTeamingCountAgainAllocation
	 */
	public static void measureRequireSum(int require_num, int successTaskRequire, int failureTaskRequire, int failureTaskNum
			, int successTeamingCount, int failureTeamingCount, int giveUpTeamingCount, int teamingCount
			, double constraintTimeInATeam, double constraintTimePerAgentInATeam
			, double constraintTimePerAgent, int succuessTeamingCountAgainAllocation){
		successTaskRequireArray[require_num] = successTaskRequire;
		failureTaskRequireArray[require_num] = failureTaskRequire;
		failureTaskNumArray[require_num] = failureTaskNum;
		successTeamingCountArray[require_num] = successTeamingCount;
		failureTeamingCountArray[require_num] = failureTeamingCount;
		giveUpTeamingCountArray[require_num] = giveUpTeamingCount;
		teamingCountArray[require_num] = teamingCount;
		constraintTimeInATeamArray[require_num] = constraintTimeInATeam;
		constraintTimePerAgentInATeamArray[require_num] = constraintTimePerAgentInATeam;
		constraintTimePerAgentArray[require_num] = constraintTimePerAgent;
		successTeamingCountAgainAllocationEndArray[require_num] = succuessTeamingCountAgainAllocation;
	}
}
