package main.teamformation;

import java.util.ArrayList;
import java.util.Arrays;

import role.Role;
import task.Task;
import team.Team;
import constant.Constant;
import agent.Agent;

public class TeamFormationMeasuredData {
	int arrayIndex;
	
	public int[] successTaskRequire = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int allSuccessTaskRequire;
	public int[] failureTaskRequire = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int allFailureTaskRequire;
	public int[] failureTaskNum = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int[] successTeamFormationNum = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int successTeamFormationNumAtEnd;
	public int allSuccessTeamFormationNum;
	public int[] failureTeamFormationNum = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int[] giveUpTeamFormationNum = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int[] tryingTeamFormationNum = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	
	public int[][] successTeamFormationNumEveryTeamSize = new int[Constant.ARRAY_SIZE_FOR_MEASURE][Constant.ARRAY_SIZE_FOR_TEAM];
	public int[] allSuccessTeamFormationNumEveryTeamSize = new int[Constant.ARRAY_SIZE_FOR_TEAM];
	public int[] bindingTimeInTeamEveryTeamSize = new int[Constant.ARRAY_SIZE_FOR_TEAM];

	public int teamExecuteTime;
	public int[] bindingTimeInTeam = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] executingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] bindingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double executingTimePerAgentInTeamAtEnd;
	public double bindingTimePerAgentInTeamAtEnd;
	
	public int tentativeTeamSize;
	public int teamSize;
	
	public int leaderMain;
	public int memberMain;
	public int neitherLeaderNorMember;
	
	public int initialTime;
	public int leaderTime;
	public int memberTime;
	public int executeTime;
	
	public int allSuccessTeamFormationEdge;
	
	public int unmarkedTaskQueueNum;
	public int taskQueueNum;
	
	public int[] markedTaskNum = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] markedTaskRequire = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] markedTaskDeadline = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	
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
	
	public void countInSuccessCase(int require, Team team) {
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
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.MEASURE_SUCCESS_AT_END_TURN_NUM) {
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
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.MEASURE_SUCCESS_AT_END_TURN_NUM) {
			executingTimePerAgentInTeamAtEnd += (double)time / (double)teamSize;
		}
	}
	
	private void countBindingTimeInTeam(int time, int teamSize) {
		bindingTimeInTeamEveryTeamSize[teamSize] += time;
		bindingTimeInTeam[arrayIndex] += time;
		bindingTimePerAgentInTeam[arrayIndex] += (double)time / (double)teamSize;
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.MEASURE_SUCCESS_AT_END_TURN_NUM) {
			bindingTimePerAgentInTeamAtEnd += (double)time / (double)teamSize;
		}
	}
	
	private void countTeamSize(int tentativeSize, int realSize) {
		tentativeTeamSize += tentativeSize;
		teamSize += realSize;
	}
	
	public void measureAtEnd(ArrayList<Agent> agents) {
		for(Agent agent : agents){
			classifyMainRoleNum(agent);
			countEachStateTime(agent);
		}
	}
	
	private void classifyMainRoleNum(Agent agent) {
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
	
	private void countEachStateTime(Agent agent) {
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
	
	public void countMarkedTask(Task markedTask) {
		markedTaskNum[arrayIndex]++;
		markedTaskRequire[arrayIndex] += markedTask.getTaskRequireSum();
		markedTaskDeadline[arrayIndex] += markedTask.getDeadlineInTask();
	}
	
	public int getAllSuccessTeamFormationEdge() {
		return allSuccessTeamFormationEdge;
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
	
	public double getAverageTeamExecuteTimeInTeam() {
		return (double)teamExecuteTime / getDivideNum(allSuccessTeamFormationNum);
	}
	
	public double getAverageExecutingTimePerAgentInTeam(int index) {
//		Log.log.debugln("executingTimePerAgentInTeam = " + executingTimePerAgentInTeam[index] + " / successTeamFormationNum = " + successTeamFormationNum[index]);
		return executingTimePerAgentInTeam[index] / getDivideNum(successTeamFormationNum[index]);
	}
	
	public double getAverageExecutingTimePerAgentInTeamAtEnd() {
		return executingTimePerAgentInTeamAtEnd / getDivideNum(successTeamFormationNumAtEnd);
	}
	
	public double getAverageBindingTimeInTeamEveryTeamSize(int teamSizeIndex) {
		return (double)bindingTimeInTeamEveryTeamSize[teamSizeIndex] / getDivideNum(allSuccessTeamFormationNumEveryTeamSize[teamSizeIndex]); 
	}
	
	public double getAverageBindingTimeInTeam(int index) {
//		Log.log.debugln("bindingTimeInTeam = " + bindingTimeInTeam[index] + " / successTeamFormationNum = " + successTeamFormationNum[index]);
		return (double)bindingTimeInTeam[index] / getDivideNum(successTeamFormationNum[index]);
	}
	
	public double getAverageBindingTimePerAgentInTeam(int index) {
//		Log.log.debugln("bindingTimePerAgentInTeam = " + bindingTimePerAgentInTeam[index] + " / successTeamFormationNum = " + successTeamFormationNum[index]);
		return bindingTimePerAgentInTeam[index] / getDivideNum(successTeamFormationNum[index]);
	}
	
	public double getAverageBindingTimePerAgentInTeamAtEnd() {
		return bindingTimePerAgentInTeamAtEnd / getDivideNum(successTeamFormationNumAtEnd);
	}
	
	public double getAverageTentativeTeamSize() {
		return (double)tentativeTeamSize / getDivideNum(allSuccessTeamFormationNum);
	}
	
	public double getAverageTeamSize() {
		return (double)teamSize / getDivideNum(allSuccessTeamFormationNum);
	}
	
	public double getAverageUnmarkedTaskQueueNum() {
		return (double)unmarkedTaskQueueNum / (double)Constant.TURN_NUM;
	}
	
	public double getAverageTaskQueueNum() {
		return (double)taskQueueNum / (double)Constant.TURN_NUM;
	}
	
	public double getAverageMarkedTaskRequire(int index) {
		return markedTaskRequire[index] / getDivideNum(markedTaskNum[index]);
	}
	
	public double getAverageMarkedTaskDeadline(int index) {
		return markedTaskDeadline[index] / getDivideNum(markedTaskNum[index]);
	}
}