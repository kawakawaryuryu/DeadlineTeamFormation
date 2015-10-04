package main;

import team.Team;

/**
 * チーム人数ごとのチーム拘束時間を保持する
 */
public class MeasureConstraintTimeData {

	/* チーム拘束時間の差分平均 */
	static int[] teamingSuccess = new int[Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];	//チームごとのチーム編成成功回数
	static int[] teamingSuccessPerTime = new int[Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];	//チーム人数ごとのチーム編成成功回数の時間推移
	static double[][] teamingSuccessPerTimeArray = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM][Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];	//チーム人数ごとのチーム編成成功回数の時間推移の結果を保持する配列
	static double[] teamConstraintTimeDifferenceSum = new double[Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];
	static double[] teamConstraintTimeDifferenceAverage = new double[Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];
	
	/**
	 * チーム拘束時間の差分を計測する
	 * チーム人数ごとに値を保持
	 * @param team
	 */
	public static void measureTeamConstraintTimeDifference(Team team){
		teamConstraintTimeDifferenceSum[team.getSize()] += team.getExecuteTimeDifference();
		teamingSuccess[team.getSize()]++;
		teamingSuccessPerTime[team.getSize()]++;
	}
	
	/**
	 * チーム拘束時間の差分の平均を求める
	 */
	public static void calculateTeamConstraintTimeDifference(){
		for(int i = 0; i < Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1; i++){
			if(teamingSuccess[i] != 0){
//				teamConstraintTimeDifferenceAverage[i] = teamConstraintTimeDifferenceSum[i] / (double)teamingSuccess[i];
				teamConstraintTimeDifferenceAverage[i] = teamConstraintTimeDifferenceSum[i];
//				System.out.println("拘束時間差分合計 = " + teamConstraintTimeDifferenceSum[i]);
//				System.out.println("index = " + teamingSuccess[i]);
//				System.out.println("拘束時間差分平均 = " + teamConstraintTimeDifferenceAverage[i]);
			}
			else{
				teamConstraintTimeDifferenceAverage[i] = 0;
			}
//			System.out.println();
			teamConstraintTimeDifferenceSum[i] = 0;
//			teamingSuccess[i] = 0;
		}
	}
	
	/**
	 * チーム人数ごとのチーム編成成功回数の時間推移
	 */
	public static void initializeTeamingSuccessPerTime(){
		for(int i = 0; i < Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1; i++){
			teamingSuccessPerTime[i] = 0;
		}
	}
	
	/**
	 * チーム人数ごとのチーム編成成功回数を計測
	 * @param require_num
	 */
	public static void measureTeamingSuccess(int require_num){
		for(int i = 0; i < Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1; i++){
			teamingSuccessPerTimeArray[require_num][i] = teamingSuccessPerTime[i];
			teamingSuccessPerTime[i] = 0;
		}
	}
	
}
