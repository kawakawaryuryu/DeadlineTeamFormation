package main.teamformation;

import java.util.ArrayList;
import java.util.Arrays;

import role.Role;
import state.InitialLeaderState;
import state.InitialMemberState;
import state.InitialRoleDecisionState;
import state.LeaderTaskExecuteState;
import state.LeaderWaitingState;
import state.MemberTaskExecuteState;
import state.MemberTeamDissolutionConfirmationState;
import state.MemberWaitingState;
import state.RoleSelectionState;
import state.SubtaskAllocationState;
import state.SubtaskReceptionState;
import state.TaskExecuteState;
import state.TaskMarkedWaitingState;
import state.TaskSelectionState;
import task.Task;
import team.Team;
import constant.Constant;
import exception.AbnormalException;
import agent.Agent;

public class TeamFormationMeasuredData {
	// 50ターンごとに計測する用のインデックス
	int arrayIndex;
	// 毎ターン計測する用のインデックス
	int turnIndex;
	
	// 時間ごとに計測するもの
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
	public int[] unmarkedByMemberSelectionNum = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int[] tryingTeamFormationNum = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	
	// チーム人数ごとに計測するもの
	public int[][] successTeamFormationNumEveryTeamSize = new int[Constant.ARRAY_SIZE_FOR_MEASURE][Constant.ARRAY_SIZE_FOR_TEAM];
	public int[] allSuccessTeamFormationNumEveryTeamSize = new int[Constant.ARRAY_SIZE_FOR_TEAM];
	public int[] bindingTimeInTeamEveryTeamSize = new int[Constant.ARRAY_SIZE_FOR_TEAM];

	// 1チームあたりの実行時間、拘束時間等を計測するもの
	public int teamExecuteTime;
	public int[] bindingTimeInTeam = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] executingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] bindingTimePerAgentInTeam = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double executingTimePerAgentInTeamAtEnd;
	public double bindingTimePerAgentInTeamAtEnd;
	
	// チーム人数
	public int tentativeTeamSize;
	public int teamSize;
	
	// 役割ごとの主に担当したエージェント数
	public int leaderMain;
	public int memberMain;
	public int neitherLeaderNorMember;
	
	// 状態ごとの留まった時間
	public int initialTime;
	public int leaderTime;
	public int memberTime;
	public int executeTime;
	
	// チーム編成を成功した中で全エージェント間のエッジ数
	public int allSuccessTeamFormationEdge;
	
	// タスクキュー内のタスクについて
	public int unmarkedTaskQueueNum;
	public int taskQueueNum;
	public double unmarkedTaskDeadline;
	public double unmarkedTaskRequire;

	// タスクのマークを外された平均回数を計測する用
	public int unmarkedTaskNum;
	public int unmarkedTaskNumByEstimationFailure;
	public int unmarkedTaskNumByTeamFormationFailure;
	public int unmarkedTaskNumByMemberDecision;
	
	// 時間ごとに計測したマークしたタスクに関するもの
	public int[] markedTaskNumEveryTurn = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] markedTaskRequireEveryTurn = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] markedTaskDeadlineEveryTurn = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public int[] unmarkedTaskNumEveryTurn = new int[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] unmarkedTaskRequireEveryTurn = new double[Constant.ARRAY_SIZE_FOR_MEASURE];
	public double[] unmarkedTaskDeadlineEveryTurn = new double[Constant.ARRAY_SIZE_FOR_MEASURE];

	// 状態ごとのエージェント数を計測したもの
	public int[] initialStateAgentNumPerTurn = new int[Constant.TURN_NUM];
	public int[] leaderOrMemberStateAgentNumPerTurn = new int[Constant.TURN_NUM];
	public int[] executeStateAgentNumPerTurn = new int[Constant.TURN_NUM];
	public int initialStateAgentNum;
	public int leaderOrMemberStateAgentNum;
	public int executeStateAgentNum;

	// 見積もり失敗の中でチームリソースが0(つまりランダムタスクを選んだこと)により失敗した回数を計測したもの
	public int estimationFailureByRandomSelection;
	public int estimationFailureByOther;
	
	public void initialize() {
		// 50ターンごとに計測する用のインデックス
		arrayIndex = 0;
		// 毎ターン計測する用のインデックス
		turnIndex = 0;
		
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
		for(int[] array : successTeamFormationNumEveryTeamSize){
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
		
		// 状態ごとの留まった時間
		initialTime = 0;
		leaderTime = 0;
		memberTime = 0;
		executeTime = 0;
		
		// チーム編成を成功した中で全エージェント間のエッジ数
		allSuccessTeamFormationEdge = 0;
		
		// タスクキュー内の数
		unmarkedTaskQueueNum = 0;
		taskQueueNum = 0;
		unmarkedTaskDeadline = 0;
		unmarkedTaskRequire = 0;

		// タスクのマークを外された平均回数を計測する用
		unmarkedTaskNum = 0;
		unmarkedTaskNumByEstimationFailure = 0;
		unmarkedTaskNumByTeamFormationFailure = 0;
		unmarkedTaskNumByMemberDecision = 0;
		
		// 時間ごとに計測したマークしたタスクに関するもの
		Arrays.fill(markedTaskNumEveryTurn, 0);
		Arrays.fill(markedTaskRequireEveryTurn, 0);
		Arrays.fill(markedTaskDeadlineEveryTurn, 0);
		Arrays.fill(unmarkedTaskNumEveryTurn, 0);
		Arrays.fill(unmarkedTaskRequireEveryTurn, 0);
		Arrays.fill(unmarkedTaskDeadlineEveryTurn, 0);

		// 状態ごとのエージェント数を計測したもの
		Arrays.fill(initialStateAgentNumPerTurn, 0);
		Arrays.fill(leaderOrMemberStateAgentNumPerTurn, 0);
		Arrays.fill(executeStateAgentNumPerTurn, 0);
		initialStateAgentNum = 0;
		leaderOrMemberStateAgentNum = 0;
		executeStateAgentNum = 0;

		// 見積もり失敗の中でチームリソースが0(つまりランダムタスクを選んだこと)により失敗した回数を計測したもの
		estimationFailureByRandomSelection = 0;
		estimationFailureByOther = 0;
	}
	
	public void addArrayIndex() {
		arrayIndex++;
	}

	public void addTurnIndex() {
		turnIndex++;
	}
	
	/**
	 * チーム編成に成功したときにカウントする
	 * @param require
	 * @param team
	 */
	public void countInSuccessCase(int require, Team team) {
		countSuccess(require);
		countSuccessTeamFormationNumEveryTeamSize(team.getTeamMate().size());
		countTeamExecuteTime(team.getTeamExecuteTime());
		countExecutingTimeInTeam(team.getExecutingTime(), team.getTeamMate().size());
		countBindingTimeInTeam((int)team.getBindingTime(), team.getTeamMate().size());
		countTeamSize(team.getTentativeTeamMate().size(), team.getTeamMate().size());
		countSuccessTeamFormationEdge(team.getTeamMate().size() - 1);
	}
	
	/**
	 * チーム編成成功回数や処理リソースなどを計測する
	 * @param require
	 */
	private void countSuccess(int require) {
		successTaskRequire[arrayIndex] += require;
		allSuccessTaskRequire += require;
		successTeamFormationNum[arrayIndex]++;
		allSuccessTeamFormationNum++;
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.END_TURN_NUM_FOR_AVERAGE) {
			successTeamFormationNumAtEnd++;
		}
	}
	
	/**
	 * タスク廃棄に関する数を計測する
	 * @param require
	 */
	public void countFailure(int require) {
		failureTaskRequire[arrayIndex] += require;
		allFailureTaskRequire += require;
		failureTaskNum[arrayIndex]++;
	}
	
	/*
	 * チーム編成失敗数を計測する
	 */
	public void countFailureTeamFormationNum() {
		failureTeamFormationNum[arrayIndex]++;
	}
	
	/**
	 * チーム編成断念数(見積もり失敗数)を計測する
	 */
	public void countGiveUpTeamFormationNum() {
		giveUpTeamFormationNum[arrayIndex]++;
	}

	/**
	 * メンバ選択によってマークを外したタスク数を計測する
	 */
	public void countUnmarkedByMemberSelectionNum() {
		unmarkedByMemberSelectionNum[arrayIndex]++;
	}
	
	/**
	 * チーム編成トライ回数を計測する
	 */
	public void countTryingTeamFormationNum() {
		tryingTeamFormationNum[arrayIndex]++;
	}
	
	/**
	 * チーム人数ごとの成功回数を計測する
	 * @param teamSize
	 */
	private void countSuccessTeamFormationNumEveryTeamSize(int teamSize) {
		successTeamFormationNumEveryTeamSize[arrayIndex][teamSize]++;
		allSuccessTeamFormationNumEveryTeamSize[teamSize]++;
	}
	
	/**
	 * チーム実行時間を計測する
	 * @param time
	 */
	private void countTeamExecuteTime(int time) {
		teamExecuteTime += time;
	}
	
	/**
	 * 1チームあたりの実行時間を計測する
	 * @param time
	 * @param teamSize
	 */
	private void countExecutingTimeInTeam(int time, int teamSize) {
		executingTimePerAgentInTeam[arrayIndex] += (double)time / (double)teamSize;
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.END_TURN_NUM_FOR_AVERAGE) {
			executingTimePerAgentInTeamAtEnd += (double)time / (double)teamSize;
		}
	}
	
	/**
	 * 1チームあたりの拘束時間を計測する
	 * @param time
	 * @param teamSize
	 */
	private void countBindingTimeInTeam(int time, int teamSize) {
		bindingTimeInTeamEveryTeamSize[teamSize] += time;
		bindingTimeInTeam[arrayIndex] += time;
		bindingTimePerAgentInTeam[arrayIndex] += (double)time / (double)teamSize;
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.END_TURN_NUM_FOR_AVERAGE) {
			bindingTimePerAgentInTeamAtEnd += (double)time / (double)teamSize;
		}
	}
	
	/**
	 * チーム人数を計測する
	 * @param tentativeSize
	 * @param realSize
	 */
	private void countTeamSize(int tentativeSize, int realSize) {
		tentativeTeamSize += tentativeSize;
		teamSize += realSize;
	}
	
	/**
	 * 1回の実験で最後に計測するものを行う
	 * @param agents
	 */
	public void measureAtEnd(ArrayList<Agent> agents) {
		for(Agent agent : agents){
			classifyMainRoleNum(agent);
			countEachStateTime(agent);
		}
	}

	/**
	 * 毎ターン計測するものを行う
	 * @param agents
	 */
	public void measureEveryTurn(ArrayList<Agent> agents) {
		for (Agent agent : agents) {
			countAgentsNum(agent);
		}
	}
	
	/**
	 * 主に担当した役割ごとのエージェント数を計測する
	 * @param agent
	 */
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
	
	/**
	 * 各状態ごとのエージェント数を計測する
	 * @param agent
	 */
	private void countEachStateTime(Agent agent) {
		initialTime += agent.getParameter().getElement(Role.INITIAL).getStateTime();
		leaderTime += agent.getParameter().getElement(Role.LEADER).getStateTime();
		memberTime += agent.getParameter().getElement(Role.MEMBER).getStateTime();
		executeTime += agent.getParameter().getElement(Role.EXECUTE).getStateTime();
	}
	
	/**
	 * チーム編成を成功したエージェント間の総エッジ数をカウントする
	 * @param edge
	 */
	private void countSuccessTeamFormationEdge(int edge) {
		allSuccessTeamFormationEdge += edge;
	}
	
	/**
	 * タスクキュー内のタスクについて計測する
	 * @param unmarkedNum
	 * @param allNum
	 * @param unmarkedTaskDeadline TODO
	 * @param unmarkedTaskRequire TODO
	 */
	public void countTaskQueue(int unmarkedNum, int allNum, double unmarkedTaskDeadline, double unmarkedTaskRequire) {
		unmarkedTaskQueueNum += unmarkedNum;
		taskQueueNum += allNum;
		this.unmarkedTaskDeadline += unmarkedTaskDeadline;
		this.unmarkedTaskRequire += unmarkedTaskRequire;
	}

	/**
	 * 見積もり失敗によってマークを外されたタスク数を計測する
	 */
	public void countUnmarkedTaskNumByEstimationFailure() {
		unmarkedTaskNum++;
		unmarkedTaskNumByEstimationFailure++;
	}

	/*
	 * チーム編成失敗によってマークを外されたタスク数を計測する
	 */
	public void countUnmarkedTaskNumByTeamFormationFailure() {
		unmarkedTaskNum++;
		unmarkedTaskNumByTeamFormationFailure++;
	}

	/**
	 * メンバ選択によってマークを外されたタスク数を計測する
	 */
	public void countUnmarkedTaskNumByMemberDecision() {
		unmarkedTaskNum++;
		unmarkedTaskNumByMemberDecision++;
	}
	
	/**
	 * マークしたタスクに関するものを計測する
	 * @param markedTask
	 */
	public void countMarkedTask(Task markedTask) {
		markedTaskNumEveryTurn[arrayIndex]++;
		markedTaskRequireEveryTurn[arrayIndex] += markedTask.getTaskRequireSum();
		markedTaskDeadlineEveryTurn[arrayIndex] += markedTask.getDeadlineInTask();
	}

	/**
	 * マークされていないタスクに関するものを計測する
	 * @param unmarkedTask
	 */
	public void countUnmarkedTask(Task unmarkedTask) {
		unmarkedTaskNumEveryTurn[arrayIndex]++;
		unmarkedTaskRequireEveryTurn[arrayIndex] += unmarkedTask.getTaskRequireSum();
		unmarkedTaskDeadlineEveryTurn[arrayIndex] += unmarkedTask.getDeadlineInTask();
	}

	/**
	 * 状態ごとのエージェント数を計測する
	 * @param agent
	 */
	private void countAgentsNum(Agent agent) {
		if (agent.getParameter().getState() == TaskSelectionState.getState()
				|| agent.getParameter().getState() == RoleSelectionState.getState()
				|| agent.getParameter().getState() == TaskMarkedWaitingState.getState()
				|| agent.getParameter().getState() == InitialRoleDecisionState.getState()
				|| agent.getParameter().getState() == InitialLeaderState.getState()
				|| agent.getParameter().getState() == InitialMemberState.getState()) {
			initialStateAgentNumPerTurn[turnIndex]++;
			initialStateAgentNum++;
		}
		else if (agent.getParameter().getState() == SubtaskAllocationState.getState()
				|| agent.getParameter().getState() == SubtaskReceptionState.getState()
				|| agent.getParameter().getState() == LeaderWaitingState.getState()
				|| agent.getParameter().getState() == MemberWaitingState.getState()) {
			leaderOrMemberStateAgentNumPerTurn[turnIndex]++;
			leaderOrMemberStateAgentNum++;
		}
		else if (agent.getParameter().getState() == LeaderTaskExecuteState.getState()
				|| agent.getParameter().getState() == MemberTaskExecuteState.getState()
				|| agent.getParameter().getState() == MemberTeamDissolutionConfirmationState.getState()
				|| agent.getParameter().getState() == TaskExecuteState.getState()) {
			executeStateAgentNumPerTurn[turnIndex]++;
			executeStateAgentNum++;
		}
		else {
			//throw new AbnormalException("このようなパターンは存在しません");
		}
	}

	public void countEstimationFailureByRandomSelection() {
		estimationFailureByRandomSelection++;
	}

	public void countEstimationFailureByOther() {
		estimationFailureByOther++;
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

	public double getAverageUnmarkedTaskNum() {
		return (double)unmarkedTaskNum / (double)Constant.TURN_NUM;
	}

	public double getAverageUnmarkedTaskDeadline() {
		return unmarkedTaskDeadline / (double)Constant.TURN_NUM;
	}

	public double getAverageUnmarkedTaskRequire() {
		return unmarkedTaskRequire / (double)Constant.TURN_NUM;
	}

	public double getAverageUnmarkedTaskNumByEstimationFailure() {
		return (double)unmarkedTaskNumByEstimationFailure / (double)Constant.TURN_NUM;
	}

	public double getAverageUnmarkedTaskNumByTeamFormationFailure() {
		return (double)unmarkedTaskNumByTeamFormationFailure / (double)Constant.TURN_NUM;
	}

	public double getAverageUnmarkedTaskNumByMemberDecision() {
		return (double)unmarkedTaskNumByMemberDecision / (double)Constant.TURN_NUM;
	}
	
	public double getAverageMarkedTaskRequireEveryTurn(int index) {
		return markedTaskRequireEveryTurn[index] / getDivideNum(markedTaskNumEveryTurn[index]);
	}
	
	public double getAverageMarkedTaskDeadlineEveryTurn(int index) {
		return markedTaskDeadlineEveryTurn[index] / getDivideNum(markedTaskNumEveryTurn[index]);
	}

	public double getAverageUnmarkedTaskRequireEveryTurn(int index) {
		return unmarkedTaskRequireEveryTurn[index] / getDivideNum(unmarkedTaskNumEveryTurn[index]);
	}

	public double getAverageUnmarkedTaskDeadlineEveryTurn(int index) {
		return unmarkedTaskDeadlineEveryTurn[index] / getDivideNum(unmarkedTaskNumEveryTurn[index]);
	}

	public double getAverageInitialStateAgentNum() {
		return (double)initialStateAgentNum / (double)Constant.TURN_NUM;
	}

	public double getAverageLeaderOrMemberStateAgentNum() {
		return (double)leaderOrMemberStateAgentNum / (double)Constant.TURN_NUM;
	}

	public double getAverageExecuteStateAgentNum() {
		return (double)executeStateAgentNum / (double)Constant.TURN_NUM;
	}

	public double getAverageEstimationFailureByRandomSelection() {
		return (double)estimationFailureByRandomSelection / (double)Constant.END_TURN_NUM;
	}

	public double getAverageEstimationFailureByOther() {
		return (double)estimationFailureByOther / (double)Constant.END_TURN_NUM;
	}

}
