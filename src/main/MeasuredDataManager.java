package main;

import java.util.Arrays;

import main.teamformation.TeamFormationInstances;
import constant.Constant;

public class MeasuredDataManager {
	
	private TeamFormationInstances instance = TeamFormationInstances.getInstance();
	
	
	
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
			successTaskRequire[i] += instance.getMeasure().successTaskRequire[i]; 
			failureTaskRequire[i] += instance.getMeasure().failureTaskRequire[i];
			failureTaskNum[i] += instance.getMeasure().failureTaskNum[i];
			successTeamFormationNum[i] += instance.getMeasure().successTeamFormationNum[i];
			failureTeamFormationNum[i] += instance.getMeasure().failureTeamFormationNum[i];
			giveUpTeamFormationNum[i] += instance.getMeasure().giveUpTeamFormationNum[i];
			tryingTeamFormationNum[i] += instance.getMeasure().tryingTeamFormationNum[i];
			for(int j = 0; j < Constant.ARRAY_SIZE_FOR_TEAM; j++){
				successTeamFormationNumEveryTeamSize[i][j] += instance.getMeasure().successTeamFormationNumEveryTeamSize[i][j];
			}
			bindingTimeInTeam[i] += instance.getMeasure().getAverageBindingTimeInTeam(i);
//			Log.log.debugln(instance.getMeasure().getAverageBindingTimeInTeam(i));
			executingTimePerAgentInTeam[i] += instance.getMeasure().getAverageExecutingTimePerAgentInTeam(i);
//			Log.log.debugln(instance.getMeasure().getAverageExecutingTimePerAgentInTeam(i));
			bindingTimePerAgentInTeam[i] += instance.getMeasure().getAverageBindingTimePerAgentInTeam(i);
//			Log.log.debugln(instance.getMeasure().getAverageBindingTimePerAgentInTeam(i));
		}
	}
	
	private void saveMeasuredDataAtEnd() {
		allSuccessTaskRequire += instance.getMeasure().allSuccessTaskRequire;
		allFailureTaskRequire += instance.getMeasure().allFailureTaskRequire;
		allSuccessTeamFormationNum += instance.getMeasure().allSuccessTeamFormationNum;
		successTeamFormationNumAtEnd += instance.getMeasure().successTeamFormationNumAtEnd;
	}
	
	private void saveMeasuredDataEveryTeamSize() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_TEAM; i++){
			allSuccessTeamFormationNumEveryTeamSize[i] += instance.getMeasure().allSuccessTeamFormationNumEveryTeamSize[i];
			bindingTimeInTeamEveryTeamSize[i] += instance.getMeasure().getAverageBindingTimeInTeamEveryTeamSize(i);
		}
	}
	
	private void saveTeamExecuteData() {
		teamExecuteTime += instance.getMeasure().getAverageTeamExecuteTimeInTeam();
		executingTimePerAgentInTeamAtEnd += instance.getMeasure().getAverageExecutingTimePerAgentInTeamAtEnd();
		bindingTimePerAgentInTeamAtEnd += instance.getMeasure().getAverageBindingTimePerAgentInTeamAtEnd();
	}
	
	private void saveTeamSizeData() {
		tentativeTeamSize += instance.getMeasure().getAverageTentativeTeamSize();
		teamSize += instance.getMeasure().getAverageTeamSize();
	}
	
	private void saveMainRoleData() {
		leaderMain += instance.getMeasure().leaderMain;
		memberMain += instance.getMeasure().memberMain;
		neitherLeaderNorMember += instance.getMeasure().neitherLeaderNorMember;
	}
	
	private void saveTaskQueueNum() {
		unmarkedTaskQueueNum += instance.getMeasure().getAverageUnmarkedTaskQueueNum();
		taskQueueNum += instance.getMeasure().getAverageTaskQueueNum();
	}
	
	private void saveMarkedTask() {
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			markedTaskRequire[i] += instance.getMeasure().getAverageMarkedTaskRequire(i);
			markedTaskDeadline[i] += instance.getMeasure().getAverageMarkedTaskDeadline(i);
		}
	}
	
}
