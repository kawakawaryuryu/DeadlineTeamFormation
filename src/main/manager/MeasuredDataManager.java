package main.manager;

import java.util.Arrays;

import main.teamformation.TeamFormationInstances;
import constant.Constant;

public class MeasuredDataManager {
	
	
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
	
	public double[][] successTeamFormationNumEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_MEASURE][Constant.ARRAY_SIZE_FOR_TEAM];
	public double[] allSuccessTeamFormationNumEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_TEAM];
	public double[] bindingTimeInTeamEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_TEAM];

	public double teamExecuteTime;
	public double[] bindingTimeInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] executingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] bindingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double executingTimePerAgentInTeamAtEnd;
	public double bindingTimePerAgentInTeamAtEnd;
	
	public double tentativeTeamSize;
	public double teamSize;
	
	public int leaderMain;
	public int memberMain;
	public int neitherLeaderNorMember;
	
	public int initialTime;
	public int leaderTime;
	public int memberTime;
	public int executeTime;
	
	public double unmarkedTaskQueueNum;
	public double taskQueueNum;

	public double unmarkedTaskNum;
	public double unmarkedTaskNumByEstimationFailure;
	public double unmarkedTaskNumByTeamFormationFailure;
	public double unmarkedTaskNumByMemberDecision;
	
	public double[] markedTaskRequire = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] markedTaskDeadline = new double[Constant.ARRAY_SIZE_FOR_MEASURE];

	public double[] initialStateAgentNumPerTurn = new double[Constant.TURN_NUM];
	public double[] leaderOrMemberStateAgentNumPerTurn = new double[Constant.TURN_NUM];
	public double[] executeStateAgentNumPerTurn = new double[Constant.TURN_NUM];
	public double initialStateAgentNum;
	public double leaderOrMemberStateAgentNum;
	public double executeStateAgentNum;
	
	MeasuredDataManager() {
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
		
		for(double[] array : successTeamFormationNumEveryTeamSize){
			Arrays.fill(array, 0);
		}
		Arrays.fill(allSuccessTeamFormationNumEveryTeamSize, 0);
		Arrays.fill(bindingTimeInTeamEveryTeamSize, 0);
		
		teamExecuteTime = 0;
		Arrays.fill(bindingTimeInTeam, 0);
		Arrays.fill(executingTimePerAgentInTeam, 0);
		Arrays.fill(bindingTimePerAgentInTeam, 0);
		executingTimePerAgentInTeamAtEnd = 0;
		bindingTimePerAgentInTeamAtEnd = 0;
		
		tentativeTeamSize = 0;
		teamSize = 0;
		
		leaderMain = 0;
		memberMain = 0;
		neitherLeaderNorMember = 0;
		
		unmarkedTaskQueueNum = 0;
		taskQueueNum = 0;

		unmarkedTaskNum = 0;
		unmarkedTaskNumByEstimationFailure = 0;
		unmarkedTaskNumByTeamFormationFailure = 0;
		unmarkedTaskNumByMemberDecision = 0;
		
		Arrays.fill(markedTaskRequire, 0);
		Arrays.fill(markedTaskDeadline, 0);

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
	
	private void saveMeasuredDataAtEnd() {
		allSuccessTaskRequire += TeamFormationInstances.getInstance().getMeasure().allSuccessTaskRequire;
		allFailureTaskRequire += TeamFormationInstances.getInstance().getMeasure().allFailureTaskRequire;
		allSuccessTeamFormationNum += TeamFormationInstances.getInstance().getMeasure().allSuccessTeamFormationNum;
		successTeamFormationNumAtEnd += TeamFormationInstances.getInstance().getMeasure().successTeamFormationNumAtEnd;
	}
	
	private void saveMeasuredDataEveryTeamSize() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_TEAM; i++){
			allSuccessTeamFormationNumEveryTeamSize[i] += TeamFormationInstances.getInstance().getMeasure().allSuccessTeamFormationNumEveryTeamSize[i];
			bindingTimeInTeamEveryTeamSize[i] += TeamFormationInstances.getInstance().getMeasure().getAverageBindingTimeInTeamEveryTeamSize(i);
		}
	}
	
	private void saveTeamExecuteData() {
		teamExecuteTime += TeamFormationInstances.getInstance().getMeasure().getAverageTeamExecuteTimeInTeam();
		executingTimePerAgentInTeamAtEnd += TeamFormationInstances.getInstance().getMeasure().getAverageExecutingTimePerAgentInTeamAtEnd();
		bindingTimePerAgentInTeamAtEnd += TeamFormationInstances.getInstance().getMeasure().getAverageBindingTimePerAgentInTeamAtEnd();
	}
	
	private void saveTeamSizeData() {
		tentativeTeamSize += TeamFormationInstances.getInstance().getMeasure().getAverageTentativeTeamSize();
		teamSize += TeamFormationInstances.getInstance().getMeasure().getAverageTeamSize();
	}
	
	private void saveMainRoleData() {
		leaderMain += TeamFormationInstances.getInstance().getMeasure().leaderMain;
		memberMain += TeamFormationInstances.getInstance().getMeasure().memberMain;
		neitherLeaderNorMember += TeamFormationInstances.getInstance().getMeasure().neitherLeaderNorMember;
	}
	
	private void saveTaskQueueNum() {
		unmarkedTaskQueueNum += TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskQueueNum();
		taskQueueNum += TeamFormationInstances.getInstance().getMeasure().getAverageTaskQueueNum();
	}

	public void saveUnmarkedTaskNum() {
		unmarkedTaskNum += TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskNum();
		unmarkedTaskNumByEstimationFailure
			+= TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskNumByEstimationFailure();
		unmarkedTaskNumByTeamFormationFailure
			+= TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskNumByTeamFormationFailure();
		unmarkedTaskNumByMemberDecision
			+= TeamFormationInstances.getInstance().getMeasure().getAverageUnmarkedTaskNumByMemberDecision();
	}
	
	private void saveMarkedTask() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			markedTaskRequire[i] += TeamFormationInstances.getInstance().getMeasure().getAverageMarkedTaskRequire(i);
			markedTaskDeadline[i] += TeamFormationInstances.getInstance().getMeasure().getAverageMarkedTaskDeadline(i);
		}
	}

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
