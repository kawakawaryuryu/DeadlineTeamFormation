package fixed.main;

import java.util.Arrays;

import fixed.constant.FixedConstant;

public class MeasuredDataManager {
	double[] successTaskRequire = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int allSuccessTaskRequire;
	double[] failureTaskRequire = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int allFailureTaskRequire;
	double[] failureTaskNum = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	double[] successTeamFormationNum = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int successTeamFormationNumAtEnd;
	int allSuccessTeamFormationNum;
	double[] failureTeamFormationNum = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	double[] giveUpTeamFormationNum = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	double[] tryingTeamFormationNum = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	
	double[][] successTeamFormationNumEveryTeamSize = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE][FixedConstant.ARRAY_SIZE_FOR_TEAM];
	double[] allSuccessTeamFormationNumEveryTeamSize = new double[FixedConstant.ARRAY_SIZE_FOR_TEAM];
	double[] bindingTimeInTeamEveryTeamSize = new double[FixedConstant.ARRAY_SIZE_FOR_TEAM];

	int teamExecuteTime;
	int[] bindingTimeInTeam = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	double[] executingTimePerAgentInTeam = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	double[] bindingTimePerAgentInTeam = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	double executingTimePerAgentInTeamAtEnd;
	double bindingTimePerAgentInTeamAtEnd;
	
	int tentativeTeamSize;
	int teamSize;
	
	int leaderMain;
	int memberMain;
	int neitherLeaderNorMember;
	
	int initialTime;
	int leaderTime;
	int memberTime;
	int executeTime;
	
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
	}
	
	public void saveAllMeasuredData() {
		saveMeasuredDataPerTurn();
		saveMeasuredDataAtEnd();
		saveMeasuredDataEveryTeamSize();
		saveTeamExecuteData();
		saveTeamSizeData();
		saveMainRoleData();
	}
	
	private void saveMeasuredDataPerTurn() {
		for(int i = 0; i < FixedConstant.ARRAY_SIZE_FOR_MEASURE; i++){
			successTaskRequire[i] += TeamFormationMain.getMeasure().successTaskRequire[i]; 
			failureTaskRequire[i] += TeamFormationMain.getMeasure().failureTaskRequire[i];
			failureTaskNum[i] += TeamFormationMain.getMeasure().failureTaskNum[i];
			successTeamFormationNum[i] += TeamFormationMain.getMeasure().successTeamFormationNum[i];
			failureTeamFormationNum[i] += TeamFormationMain.getMeasure().failureTeamFormationNum[i];
			giveUpTeamFormationNum[i] += TeamFormationMain.getMeasure().giveUpTeamFormationNum[i];
			tryingTeamFormationNum[i] += TeamFormationMain.getMeasure().tryingTeamFormationNum[i];
			for(int j = 0; j < FixedConstant.ARRAY_SIZE_FOR_TEAM; j++){
				successTeamFormationNumEveryTeamSize[i][j] += TeamFormationMain.getMeasure().successTeamFormationNumEveryTeamSize[i][j];
			}
			bindingTimeInTeam[i] += TeamFormationMain.getMeasure().getAverageBindingTimeInTeam(i);
//			System.out.println(TeamFormationMain.getMeasure().getAverageBindingTimeInTeam(i));
			executingTimePerAgentInTeam[i] += TeamFormationMain.getMeasure().getAverageExecutingTimePerAgentInTeam(i);
//			System.out.println(TeamFormationMain.getMeasure().getAverageExecutingTimePerAgentInTeam(i));
			bindingTimePerAgentInTeam[i] += TeamFormationMain.getMeasure().getAverageBindingTimePerAgentInTeam(i);
//			System.out.println(TeamFormationMain.getMeasure().getAverageBindingTimePerAgentInTeam(i));
		}
	}
	
	private void saveMeasuredDataAtEnd() {
		allSuccessTaskRequire += TeamFormationMain.getMeasure().allSuccessTaskRequire;
		allFailureTaskRequire += TeamFormationMain.getMeasure().allFailureTaskRequire;
		allSuccessTeamFormationNum += TeamFormationMain.getMeasure().allSuccessTeamFormationNum;
		successTeamFormationNumAtEnd += TeamFormationMain.getMeasure().successTeamFormationNumAtEnd;
	}
	
	private void saveMeasuredDataEveryTeamSize() {
		for(int i = 0; i < FixedConstant.ARRAY_SIZE_FOR_TEAM; i++){
			allSuccessTeamFormationNumEveryTeamSize[i] += TeamFormationMain.getMeasure().allSuccessTeamFormationNumEveryTeamSize[i];
			bindingTimeInTeamEveryTeamSize[i] += TeamFormationMain.getMeasure().getAverageBindingTimeInTeamEveryTeamSize(i);
		}
	}
	
	private void saveTeamExecuteData() {
		teamExecuteTime += TeamFormationMain.getMeasure().getAverageTeamExecuteTimeInTeam();
		executingTimePerAgentInTeamAtEnd += TeamFormationMain.getMeasure().getAverageExecutingTimePerAgentInTeamAtEnd();
		bindingTimePerAgentInTeamAtEnd += TeamFormationMain.getMeasure().getAverageBindingTimePerAgentInTeamAtEnd();
	}
	
	private void saveTeamSizeData() {
		tentativeTeamSize += TeamFormationMain.getMeasure().getAverageTentativeTeamSize();
		teamSize += TeamFormationMain.getMeasure().getAverageTeamSize();
	}
	
	private void saveMainRoleData() {
		leaderMain += TeamFormationMain.getMeasure().leaderMain;
		memberMain += TeamFormationMain.getMeasure().memberMain;
		neitherLeaderNorMember += TeamFormationMain.getMeasure().neitherLeaderNorMember;
	}
	
}
