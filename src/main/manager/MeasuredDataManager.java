package main.manager;

import java.util.Arrays;

import main.teamformation.TeamFormationInstances;
import constant.Constant;

public class MeasuredDataManager {
	
	
	// 時間ごとに計測するもの
	public double[] successTaskRequire = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int allSuccessTaskRequire;
	public double[] failureTaskRequire = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int allFailureTaskRequire;
	public double[] failureTaskNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] successTeamFormationNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int successTeamFormationNumAtEnd;
	public int allSuccessTeamFormationNum;
	public double[] failureTeamFormationNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] giveUpTeamFormationNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] unmarkedByMemberSelectionNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] tryingTeamFormationNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	
	// チーム人数ごとに計測するもの
	public double[][] successTeamFormationNumEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_MEASURE][Constant.ARRAY_SIZE_FOR_TEAM];
	public double[] allSuccessTeamFormationNumEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_TEAM];
	public double[] bindingTimeInTeamEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_TEAM];

	// 1チームあたりの実行時間、拘束時間等を計測するもの
	public double teamExecuteTime;
	public double[] bindingTimeInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] executingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] bindingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double executingTimePerAgentInTeamAtEnd;
	public double bindingTimePerAgentInTeamAtEnd;
	
	// チーム人数
	public double tentativeTeamSize;
	public double teamSize;
	
	// 役割ごとの主に担当したエージェント数
	public int leaderMain;
	public int memberMain;
	public int neitherLeaderNorMember;
	
	// 状態ごとの留まった時間
	public int initialTime;
	public int leaderTime;
	public int memberTime;
	public int executeTime;
	
	// タスクキュー内の数
	public double unmarkedTaskQueueNum;
	public double taskQueueNum;

	// タスクのマークを外された平均回数を計測する用
	public double unmarkedTaskNum;
	public double unmarkedTaskNumByEstimationFailure;
	public double unmarkedTaskNumByTeamFormationFailure;
	public double unmarkedTaskNumByMemberDecision;
	
	// 時間ごとに計測したマークしたタスクに関するもの
	public double[] markedTaskRequire = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] markedTaskDeadline = new double[Constant.ARRAY_SIZE_FOR_MEASURE];

	// 状態ごとのエージェント数を計測したもの
	public double[] initialStateAgentNumPerTurn = new double[Constant.TURN_NUM];
	public double[] leaderOrMemberStateAgentNumPerTurn = new double[Constant.TURN_NUM];
	public double[] executeStateAgentNumPerTurn = new double[Constant.TURN_NUM];
	public double initialStateAgentNum;
	public double leaderOrMemberStateAgentNum;
	public double executeStateAgentNum;
	
	MeasuredDataManager() {
		// 時間ごとに計測するもの
		Arrays.fill(successTaskRequire, 0);
		allSuccessTaskRequire = 0;
		Arrays.fill(failureTaskRequire, 0);
		allFailureTaskRequire = 0;
		Arrays.fill(failureTaskNum, 0);
		Arrays.fill(successTeamFormationNum, 0);
		successTeamFormationNumAtEnd = 0;
		allSuccessTeamFormationNum = 0;
		Arrays.fill(failureTeamFormationNum, 0);
		Arrays.fill(giveUpTeamFormationNum, 0);
		Arrays.fill(unmarkedByMemberSelectionNum, 0);
		Arrays.fill(tryingTeamFormationNum, 0);
		
		// チーム人数ごとに計測するもの
		for(double[] array : successTeamFormationNumEveryTeamSize){
			Arrays.fill(array, 0);
		}
		Arrays.fill(allSuccessTeamFormationNumEveryTeamSize, 0);
		Arrays.fill(bindingTimeInTeamEveryTeamSize, 0);
		
		// 1チームあたりの実行時間、拘束時間等を計測するもの
		teamExecuteTime = 0;
		Arrays.fill(bindingTimeInTeam, 0);
		Arrays.fill(executingTimePerAgentInTeam, 0);
		Arrays.fill(bindingTimePerAgentInTeam, 0);
		executingTimePerAgentInTeamAtEnd = 0;
		bindingTimePerAgentInTeamAtEnd = 0;
		
		// チーム人数
		tentativeTeamSize = 0;
		teamSize = 0;
		
		// 役割ごとの主に担当したエージェント数
		leaderMain = 0;
		memberMain = 0;
		neitherLeaderNorMember = 0;
		
		// タスクキュー内の数
		unmarkedTaskQueueNum = 0;
		taskQueueNum = 0;

		// タスクのマークを外された平均回数を計測する用
		unmarkedTaskNum = 0;
		unmarkedTaskNumByEstimationFailure = 0;
		unmarkedTaskNumByTeamFormationFailure = 0;
		unmarkedTaskNumByMemberDecision = 0;
		
		// 時間ごとに計測したマークしたタスクに関するもの
		Arrays.fill(markedTaskRequire, 0);
		Arrays.fill(markedTaskDeadline, 0);

		// 状態ごとのエージェント数を計測したもの
		Arrays.fill(initialStateAgentNumPerTurn, 0);
		Arrays.fill(leaderOrMemberStateAgentNumPerTurn, 0);
		Arrays.fill(executeStateAgentNumPerTurn, 0);
		initialStateAgentNum = 0;
		leaderOrMemberStateAgentNum = 0;
		executeStateAgentNum = 0;
	}
	
	public void saveAllMeasuredData() {
		saveMeasuredDataPerTurn();
		saveMeasuredDataAtEnd();
		saveMeasuredDataEveryTeamSize();
		saveTeamExecuteData();
		saveTeamSizeData();
		saveMainRoleData();
		saveTaskQueueNum();
		saveUnmarkedTaskNum();
		saveMarkedTask();
		saveAgentsNum();
	}
	
	/**
	 * 時間ごとに計測するものを変数に保存する
	 */
	private void saveMeasuredDataPerTurn() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			successTaskRequire[i] += TeamFormationInstances.getInstance().getMeasure().successTaskRequire[i]; 
			failureTaskRequire[i] += TeamFormationInstances.getInstance().getMeasure().failureTaskRequire[i];
			failureTaskNum[i] += TeamFormationInstances.getInstance().getMeasure().failureTaskNum[i];
			successTeamFormationNum[i] += TeamFormationInstances.getInstance().getMeasure().successTeamFormationNum[i];
			failureTeamFormationNum[i] += TeamFormationInstances.getInstance().getMeasure().failureTeamFormationNum[i];
			giveUpTeamFormationNum[i] += TeamFormationInstances.getInstance().getMeasure().giveUpTeamFormationNum[i];
			unmarkedByMemberSelectionNum[i] += TeamFormationInstances.getInstance().getMeasure().unmarkedByMemberSelectionNum[i];
			tryingTeamFormationNum[i] += TeamFormationInstances.getInstance().getMeasure().tryingTeamFormationNum[i];
			for(int j = 0; j < Constant.ARRAY_SIZE_FOR_TEAM; j++){
				successTeamFormationNumEveryTeamSize[i][j] += TeamFormationInstances.getInstance().getMeasure().successTeamFormationNumEveryTeamSize[i][j];
			}
			bindingTimeInTeam[i] += TeamFormationInstances.getInstance().getMeasure().getAverageBindingTimeInTeam(i);
//			Log.log.debugln(TeamFormationInstances.getInstance().getMeasure().getAverageBindingTimeInTeam(i));
			executingTimePerAgentInTeam[i] += TeamFormationInstances.getInstance().getMeasure().getAverageExecutingTimePerAgentInTeam(i);
//			Log.log.debugln(TeamFormationInstances.getInstance().getMeasure().getAverageExecutingTimePerAgentInTeam(i));
			bindingTimePerAgentInTeam[i] += TeamFormationInstances.getInstance().getMeasure().getAverageBindingTimePerAgentInTeam(i);
//			Log.log.debugln(TeamFormationInstances.getInstance().getMeasure().getAverageBindingTimePerAgentInTeam(i));
		}
	}
	
	/**
	 * 1回の実験ごとに計測するものを変数に保存する
	 */
	private void saveMeasuredDataAtEnd() {
		allSuccessTaskRequire += TeamFormationInstances.getInstance().getMeasure().allSuccessTaskRequire;
		allFailureTaskRequire += TeamFormationInstances.getInstance().getMeasure().allFailureTaskRequire;
		allSuccessTeamFormationNum += TeamFormationInstances.getInstance().getMeasure().allSuccessTeamFormationNum;
		successTeamFormationNumAtEnd += TeamFormationInstances.getInstance().getMeasure().successTeamFormationNumAtEnd;
	}
	
	/**
	 * チーム人数ごとに計測するものを変数に保存する
	 */
	private void saveMeasuredDataEveryTeamSize() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_TEAM; i++){
			allSuccessTeamFormationNumEveryTeamSize[i] += TeamFormationInstances.getInstance().getMeasure().allSuccessTeamFormationNumEveryTeamSize[i];
			bindingTimeInTeamEveryTeamSize[i] += TeamFormationInstances.getInstance().getMeasure().getAverageBindingTimeInTeamEveryTeamSize(i);
		}
	}
	
	/**
	 * チーム実行に関するものを変数に保存する
	 */
	private void saveTeamExecuteData() {
		teamExecuteTime += TeamFormationInstances.getInstance().getMeasure().getAverageTeamExecuteTimeInTeam();
		executingTimePerAgentInTeamAtEnd += TeamFormationInstances.getInstance().getMeasure().getAverageExecutingTimePerAgentInTeamAtEnd();
		bindingTimePerAgentInTeamAtEnd += TeamFormationInstances.getInstance().getMeasure().getAverageBindingTimePerAgentInTeamAtEnd();
	}
	
	/**
	 * チーム人数を変数に保存する　
	 */
	private void saveTeamSizeData() {
		tentativeTeamSize += TeamFormationInstances.getInstance().getMeasure().getAverageTentativeTeamSize();
		teamSize += TeamFormationInstances.getInstance().getMeasure().getAverageTeamSize();
	}
	
	/**
	 * 役割ごとの主に担当したエージェント数を変数に保存する
	 */
	private void saveMainRoleData() {
		leaderMain += TeamFormationInstances.getInstance().getMeasure().leaderMain;
		memberMain += TeamFormationInstances.getInstance().getMeasure().memberMain;
		neitherLeaderNorMember += TeamFormationInstances.getInstance().getMeasure().neitherLeaderNorMember;
	}
	
	/**
	 * タスクキュー内の数を変数に保存する
	 */
	private void saveTaskQueueNum() {
		unmarkedTaskQueueNum += TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskQueueNum();
		taskQueueNum += TeamFormationInstances.getInstance().getMeasure().getAverageTaskQueueNum();
	}

	/**
	 * タスクのマークを外された平均回数を変数に保存する
	 */
	public void saveUnmarkedTaskNum() {
		unmarkedTaskNum += TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskNum();
		unmarkedTaskNumByEstimationFailure
			+= TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskNumByEstimationFailure();
		unmarkedTaskNumByTeamFormationFailure
			+= TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskNumByTeamFormationFailure();
		unmarkedTaskNumByMemberDecision
			+= TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskNumByMemberDecision();
	}
	
	/**
	 * 時間ごとに計測したマークしたタスクに関するものを変数に保存する
	 */
	private void saveMarkedTask() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			markedTaskRequire[i] += TeamFormationInstances.getInstance().getMeasure().getAverageMarkedTaskRequire(i);
			markedTaskDeadline[i] += TeamFormationInstances.getInstance().getMeasure().getAverageMarkedTaskDeadline(i);
		}
	}

	/**
	 * 状態ごとのエージェント数を計測したものを変数に保存する
	 */
	private void saveAgentsNum() {
		for (int i = 0; i < Constant.TURN_NUM; i++) {
			initialStateAgentNumPerTurn[i] += TeamFormationInstances.getInstance().getMeasure().initialStateAgentNumPerTurn[i];
			leaderOrMemberStateAgentNumPerTurn[i] += TeamFormationInstances.getInstance().getMeasure().leaderOrMemberStateAgentNumPerTurn[i];
			executeStateAgentNumPerTurn[i] += TeamFormationInstances.getInstance().getMeasure().executeStateAgentNumPerTurn[i];
		}
		initialStateAgentNum += TeamFormationInstances.getInstance().getMeasure().getAverageInitialStateAgentNum();
		leaderOrMemberStateAgentNum += TeamFormationInstances.getInstance().getMeasure().getAverageLeaderOrMemberStateAgentNum();
		executeStateAgentNum += TeamFormationInstances.getInstance().getMeasure().getAverageExecuteStateAgentNum();
	}
	
}
