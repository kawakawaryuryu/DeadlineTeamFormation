package main;

import java.util.ArrayList;
import java.util.Arrays;

import role.Role;
import task.FixedTask;
import team.FixedTeam;
import constant.FixedConstant;
import agent.FixedAgent;

public class TeamFormationMeasuredData {
	int arrayIndex;
	
	int[] successTaskRequire = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int allSuccessTaskRequire;
	int[] failureTaskRequire = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int allFailureTaskRequire;
	int[] failureTaskNum = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int[] successTeamFormationNum = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int successTeamFormationNumAtEnd;
	int allSuccessTeamFormationNum;
	int[] failureTeamFormationNum = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int[] giveUpTeamFormationNum = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	int[] tryingTeamFormationNum = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	
	int[][] successTeamFormationNumEveryTeamSize = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE][FixedConstant.ARRAY_SIZE_FOR_TEAM];
	int[] allSuccessTeamFormationNumEveryTeamSize = new int[FixedConstant.ARRAY_SIZE_FOR_TEAM];
	int[] bindingTimeInTeamEveryTeamSize = new int[FixedConstant.ARRAY_SIZE_FOR_TEAM];

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
	
	int allSuccessTeamFormationEdge;
	
	int unmarkedTaskQueueNum;
	int taskQueueNum;
	
	int[] markedTaskNum = new int[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	double[] markedTaskRequire = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	double[] markedTaskDeadline = new double[FixedConstant.ARRAY_SIZE_FOR_MEASURE];
	
	public void initialize() {
		arrayIndex = 0;
		
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
		
		for(int[] array : successTeamFormationNumEveryTeamSize){
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
		
		initialTime = 0;
		leaderTime = 0;
		memberTime = 0;
		executeTime = 0;
		
		allSuccessTeamFormationEdge = 0;
		
		unmarkedTaskQueueNum = 0;
		taskQueueNum = 0;
		
		Arrays.fill(markedTaskNum, 0);
		Arrays.fill(markedTaskRequire, 0);
		Arrays.fill(markedTaskDeadline, 0);
	}
	
	public void addArrayIndex() {
		arrayIndex++;
	}
	
	public void countInSuccessCase(int require, FixedTeam team) {
		countSuccess(require);
		countSuccessTeamFormationNumEveryTeamSize(team.getTeamMate().size());
		countTeamExecuteTime(team.getTeamExecuteTime());
		countExecutingTimeInTeam(team.getExecutingTime(), team.getTeamMate().size());
		countBindingTimeInTeam((int)team.getBindingTime(), team.getTeamMate().size());
		countTeamSize(team.getTentativeTeamMate().size(), team.getTeamMate().size());
		countSuccessTeamFormationEdge(team.getTeamMate().size() - 1);
	}
	
	private void countSuccess(int require) {
		successTaskRequire[arrayIndex] += require;
		allSuccessTaskRequire += require;
		successTeamFormationNum[arrayIndex]++;
		allSuccessTeamFormationNum++;
		if(FixedConstant.TURN_NUM - TeamFormationMain.getTurn() < FixedConstant.MEASURE_SUCCESS_AT_END_TURN_NUM) {
			successTeamFormationNumAtEnd++;
		}
	}
	
	public void countFailure(int require) {
		failureTaskRequire[arrayIndex] += require;
		allFailureTaskRequire += require;
		failureTaskNum[arrayIndex]++;
	}
	
	public void countFailureTeamFormationNum() {
		failureTeamFormationNum[arrayIndex]++;
	}
	
	public void countGiveUpTeamFormationNum() {
		giveUpTeamFormationNum[arrayIndex]++;
	}
	
	public void countTryingTeamFormationNum() {
		tryingTeamFormationNum[arrayIndex]++;
	}
	
	private void countSuccessTeamFormationNumEveryTeamSize(int teamSize) {
		successTeamFormationNumEveryTeamSize[arrayIndex][teamSize]++;
		allSuccessTeamFormationNumEveryTeamSize[teamSize]++;
	}
	
	private void countTeamExecuteTime(int time) {
		teamExecuteTime += time;
	}
	
	private void countExecutingTimeInTeam(int time, int teamSize) {
		executingTimePerAgentInTeam[arrayIndex] += (double)time / (double)teamSize;
		if(FixedConstant.TURN_NUM - TeamFormationMain.getTurn() < FixedConstant.MEASURE_SUCCESS_AT_END_TURN_NUM) {
			executingTimePerAgentInTeamAtEnd += (double)time / (double)teamSize;
		}
	}
	
	private void countBindingTimeInTeam(int time, int teamSize) {
		bindingTimeInTeamEveryTeamSize[teamSize] += time;
		bindingTimeInTeam[arrayIndex] += time;
		bindingTimePerAgentInTeam[arrayIndex] += (double)time / (double)teamSize;
		if(FixedConstant.TURN_NUM - TeamFormationMain.getTurn() < FixedConstant.MEASURE_SUCCESS_AT_END_TURN_NUM) {
			bindingTimePerAgentInTeamAtEnd += (double)time / (double)teamSize;
		}
	}
	
	private void countTeamSize(int tentativeSize, int realSize) {
		tentativeTeamSize += tentativeSize;
		teamSize += realSize;
	}
	
	public void measureAtEnd(ArrayList<FixedAgent> agents) {
		for(FixedAgent agent : agents){
			classifyMainRoleNum(agent);
			countEachStateTime(agent);
		}
	}
	
	private void classifyMainRoleNum(FixedAgent agent) {
		int leaderNum = agent.getParameter().getElement(Role.LEADER).getRoleNum();
		int memberNum = agent.getParameter().getElement(Role.MEMBER).getRoleNum();
		if(leaderNum > memberNum * 2){
			leaderMain++;
		}
		else if(memberNum > leaderNum * 2){
			memberMain++;
		}
		else{
			neitherLeaderNorMember++;
		}
	}
	
	private void countEachStateTime(FixedAgent agent) {
		initialTime += agent.getParameter().getElement(Role.INITIAL).getStateTime();
		leaderTime += agent.getParameter().getElement(Role.LEADER).getStateTime();
		memberTime += agent.getParameter().getElement(Role.MEMBER).getStateTime();
		executeTime += agent.getParameter().getElement(Role.EXECUTE).getStateTime();
	}
	
	private void countSuccessTeamFormationEdge(int edge) {
		allSuccessTeamFormationEdge += edge;
	}
	
	public void countTaskQueueNum(int unmarkedNum, int allNum) {
		unmarkedTaskQueueNum += unmarkedNum;
		taskQueueNum += allNum;
	}
	
	public void countMarkedTask(FixedTask markedTask) {
		markedTaskNum[arrayIndex]++;
		markedTaskRequire[arrayIndex] += markedTask.getTaskRequireSum();
		markedTaskDeadline[arrayIndex] += markedTask.getDeadlineInTask();
	}
	
	/**
	 * 割る数が0にならないようにする
	 * @param num
	 * @return
	 */
	private double getDivideNum(int num) {
		if(num == 0){
			return 1;
		}
		else{
			return (double)num;
		}
	}
	
	double getAverageTeamExecuteTimeInTeam() {
		return (double)teamExecuteTime / getDivideNum(allSuccessTeamFormationNum);
	}
	
	double getAverageExecutingTimePerAgentInTeam(int index) {
//		System.out.println("executingTimePerAgentInTeam = " + executingTimePerAgentInTeam[index] + " / successTeamFormationNum = " + successTeamFormationNum[index]);
		return executingTimePerAgentInTeam[index] / getDivideNum(successTeamFormationNum[index]);
	}
	
	double getAverageExecutingTimePerAgentInTeamAtEnd() {
		return executingTimePerAgentInTeamAtEnd / getDivideNum(successTeamFormationNumAtEnd);
	}
	
	double getAverageBindingTimeInTeamEveryTeamSize(int teamSizeIndex) {
		return (double)bindingTimeInTeamEveryTeamSize[teamSizeIndex] / getDivideNum(allSuccessTeamFormationNumEveryTeamSize[teamSizeIndex]); 
	}
	
	double getAverageBindingTimeInTeam(int index) {
//		System.out.println("bindingTimeInTeam = " + bindingTimeInTeam[index] + " / successTeamFormationNum = " + successTeamFormationNum[index]);
		return (double)bindingTimeInTeam[index] / getDivideNum(successTeamFormationNum[index]);
	}
	
	double getAverageBindingTimePerAgentInTeam(int index) {
//		System.out.println("bindingTimePerAgentInTeam = " + bindingTimePerAgentInTeam[index] + " / successTeamFormationNum = " + successTeamFormationNum[index]);
		return bindingTimePerAgentInTeam[index] / getDivideNum(successTeamFormationNum[index]);
	}
	
	double getAverageBindingTimePerAgentInTeamAtEnd() {
		return bindingTimePerAgentInTeamAtEnd / getDivideNum(successTeamFormationNumAtEnd);
	}
	
	double getAverageTentativeTeamSize() {
		return (double)tentativeTeamSize / getDivideNum(allSuccessTeamFormationNum);
	}
	
	double getAverageTeamSize() {
		return (double)teamSize / getDivideNum(allSuccessTeamFormationNum);
	}
	
	double getAverageUnmarkedTaskQueueNum() {
		return (double)unmarkedTaskQueueNum / (double)FixedConstant.TURN_NUM;
	}
	
	double getAverageTaskQueueNum() {
		return (double)taskQueueNum / (double)FixedConstant.TURN_NUM;
	}
	
	double getAverageMarkedTaskRequire(int index) {
		return markedTaskRequire[index] / getDivideNum(markedTaskNum[index]);
	}
	
	double getAverageMarkedTaskDeadline(int index) {
		return markedTaskDeadline[index] / getDivideNum(markedTaskNum[index]);
	}
}
