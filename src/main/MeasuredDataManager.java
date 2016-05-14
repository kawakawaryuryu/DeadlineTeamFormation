package main;

import java.util.Arrays;

import main.teamformation.TeamFormationInstances;
import constant.Constant;

public class MeasuredDataManager {
	
	
	double[] successTaskRequire = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	int allSuccessTaskRequire;
	double[] failureTaskRequire = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	int allFailureTaskRequire;
	double[] failureTaskNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	double[] successTeamFormationNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	int successTeamFormationNumAtEnd;
	int allSuccessTeamFormationNum;
	double[] failureTeamFormationNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	double[] giveUpTeamFormationNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	double[] tryingTeamFormationNum = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	
	double[][] successTeamFormationNumEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_MEASURE][Constant.ARRAY_SIZE_FOR_TEAM];
	double[] allSuccessTeamFormationNumEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_TEAM];
	double[] bindingTimeInTeamEveryTeamSize = new double[Constant.ARRAY_SIZE_FOR_TEAM];

	double teamExecuteTime;
	double[] bindingTimeInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	double[] executingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	double[] bindingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	double executingTimePerAgentInTeamAtEnd;
	double bindingTimePerAgentInTeamAtEnd;
	
	double tentativeTeamSize;
	double teamSize;
	
	int leaderMain;
	int memberMain;
	int neitherLeaderNorMember;
	
	int initialTime;
	int leaderTime;
	int memberTime;
	int executeTime;
	
	double unmarkedTaskQueueNum;
	double taskQueueNum;
	
	double[] markedTaskRequire = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	double[] markedTaskDeadline = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	
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
		
		Arrays.fill(markedTaskRequire, 0);
		Arrays.fill(markedTaskDeadline, 0);
	}
	
	public void saveAllMeasuredData() {
		saveMeasuredDataPerTurn();
		saveMeasuredDataAtEnd();
		saveMeasuredDataEveryTeamSize();
		saveTeamExecuteData();
		saveTeamSizeData();
		saveMainRoleData();
		saveTaskQueueNum();
		saveMarkedTask();
	}
	
	private void saveMeasuredDataPerTurn() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			successTaskRequire[i] += TeamFormationInstances.getInstance().getMeasure().successTaskRequire[i]; 
			failureTaskRequire[i] += TeamFormationInstances.getInstance().getMeasure().failureTaskRequire[i];
			failureTaskNum[i] += TeamFormationInstances.getInstance().getMeasure().failureTaskNum[i];
			successTeamFormationNum[i] += TeamFormationInstances.getInstance().getMeasure().successTeamFormationNum[i];
			failureTeamFormationNum[i] += TeamFormationInstances.getInstance().getMeasure().failureTeamFormationNum[i];
			giveUpTeamFormationNum[i] += TeamFormationInstances.getInstance().getMeasure().giveUpTeamFormationNum[i];
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
	
	private void saveMarkedTask() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			markedTaskRequire[i] += TeamFormationInstances.getInstance().getMeasure().getAverageMarkedTaskRequire(i);
			markedTaskDeadline[i] += TeamFormationInstances.getInstance().getMeasure().getAverageMarkedTaskDeadline(i);
		}
	}
	
}
