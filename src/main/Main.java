package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import state.ExecuteState;
import state.InitialMarkingTaskState;
import state.InitialRoleSelectState;
import state.InitialWaitState;
import state.LeaderState;
import state.LeaderWaitState;
import state.MemberState;
import state.MemberWaitState;
import task.Task;
import agent.Agent;
import agent.LearningExpectedRewardPerTurnAgent1;
import agent.LearningExpectedRewardPerTurnAgent2;
import agent.LearningExpectedRewardPerTurnAgent3;

public class Main {
	
//	private static Random agent_random = new Random(1000000009);	//1000000009エージェント生成のためのランダムインスタンス
//	private static Random task_random = new Random(3);	//3タスク生成のためのランダムインスタンス
//	private static Random deadline_random = new Random(5);	//5デッドラインのランダムインスタンス
//	private static Random initial_random = new Random(7);	//7initialAgentsリストシャッフルのためのランダムインスタンス
//	private static Random leader_random = new Random(11);	//11leaderAgentsリストシャッフルのためのランダムインスタンス
//	private static Random member_random = new Random(13);	//13memberAgentsリストシャッフルのためのランダムインスタンス
//	private static Random executing_random = new Random(17);	//17executingAgentsリストシャッフルのためのランダムインスタンス
	
	private static List<Agent> agents = new ArrayList<Agent>();					//エージェントを管理するリスト
	private static List<Agent> initialAgents = new ArrayList<Agent>();			//初期状態のエージェントリスト
	private static List<Agent> markingTaskAgents = new ArrayList<Agent>();		//初期状態でタスクをマークしたエージェントリスト
	private static List<Agent> notMarkingTaskAgents = new ArrayList<Agent>();	//初期状態でタスクをマークしなかったエージェントリスト
	private static List<Agent> initialWaitAgents = new ArrayList<Agent>();		//初期待機状態のエージェントリスト
	private static List<Agent> leaderAgents = new ArrayList<Agent>();			//リーダ提案送信状態のエージェントリスト
	private static List<Agent> leaderWaitAgents = new ArrayList<Agent>();		//リーダ待機状態のエージェントリスト
	private static List<Agent> memberAgents = new ArrayList<Agent>();			//メンバ提案受託状態のエージェントリスト
	private static List<Agent> memberWaitAgents = new ArrayList<Agent>();		//メンバ待機状態のエージェントリスト
	private static List<Agent> executingAgents = new ArrayList<Agent>();		//タスク実行状態のエージェントリスト
	private static List<Task> taskQueue = new ArrayList<Task>();				//タスクを管理するキュー
//	private static Queue<Task> failure_task_queue = new LinkedList<Task>();		//廃棄タスクキュー
	
	private static int successTaskRequire = 0;		//25ターン毎のタスク処理リソース量
	private static int allSuccessTaskRequire = 0;	//総タスク処理リソース量
	private static int failureTaskRequire = 0;		//25ターン毎の廃棄タスクリソース量
	private static int allFailureTaskRequire = 0;	//総廃棄タスクリソース量
	private static int failureTaskNum = 0;			//廃棄タスク数
	private static int successTeamingCount = 0;		//25ターン毎のチーム編成成功回数
	private static int allSuccessTeamingCount = 0;	//全チーム編成成功回数
	private static int successTeamingCountAtEnd = 0;	//最後の数ターンのチーム編成成功回数
	private static int successTeamingCountAgainAllocation = 0;	//再割り当てを行ってチーム編成が成功した回数
	private static int failureTeamingCount = 0;		//25ターン毎のチーム編成失敗回数
	private static int giveUpTeamingCount = 0;		//タスク取るのを諦める回数
	private static int teamingCount = 0;			//チーム編成実施回数
	private static double constraintTimeInATeam = 0;			//1回のチーム編成における不要な拘束時間
	private static double constraintTimePerAgentInATeam = 0;	//1回のチーム編成における1人あたりの不要な拘束時間
	private static double constraintTimePerAgent = 0;	//1人あたりの不要な拘束時間
	private static double executingTimePerAgentInATeamAtEnd = 0;	//最後の数ターンの1チーム中の1人あたりの平均処理時間
	private static double constraintTimePerAgentInATeamAtEnd = 0;	//最後の数ターンの1チーム中の1人あたりの平均拘束時間
	
	private static int temporaryTeamSizeSum = 0;	//仮チームのサイズ合計
	private static int teamSizeSum = 0;				//チームのサイズ合計
	
	private static int leaderMain = 0;				//リーダを主に担当したエージェント数
	private static int memberMain = 0;				//メンバを主に担当したエージェント数
	private static int neitherLeaderNorMember = 0;	//主に担当した役割がリーダでもメンバでもないエージェント数
	
	private static int teamExecuteTimeSum = 0;	//チームでのタスク処理時間合計
	
	private static double initialStateTimeSum = 0.0;			//1人あたりの初期状態にいる時間平均
	private static double leaderStateTimeSum = 0.0;				//1人あたりのリーダ状態にいる時間平均
	private static double memberStateTimeSum = 0.0;				//1人あたりのメンバ状態にいる時間平均
	private static double executeStateTimeSum = 0.0;			//1人あたりの実行状態にいる時間平均
	private static double executeStateTimeOnceTime = 0.0;		//1人あたりの1回のチーム編成における実行状態にかかる時間平均
	private static double executeStateExecutingTimeSum = 0.0;	//1人あたりの1回のチーム編成における実行状態においてタスクを処理していた時間平均
	private static double executeStateConstraintTimeSum = 0.0;	//1人あたりの1回のチーム編成における実行状態において無駄に拘束されている時間平均
	
	private static double executeStateExecutingTimeAtEndSum = 0.0;	//最後の数ターンの処理していた時間平均
	private static double executeStateConstraintTimeAtEndSum = 0.0;	//最後の数ターンの拘束されていた時間平均
	
	public static double[] agentAbilityAverage = new double[Constant.RESOURCE_NUM];	//エージェントのリソースの平均
	
	private static int successTeamingEdgeForVisualization = 0;	//エージェント間の総エッジ数
	
	private static double[] stateAgentNumPerTurnAgerage = new double[Constant.STATE_NUM];	//一定時間ごとの暇or忙しいエージェント数の平均
	private static long[] stateAgentNumPerTurnSum = new long[Constant.STATE_NUM];	//一定時間ごとの暇or忙しいエージェント数の合計
	private static double[] stateAgentResourcesPerTurnAverage = new double[Constant.STATE_NUM];	//一定時間ごとの暇or忙しい1人あたりのエージェントリソースの平均
	private static long[] stateAgentResourcesPerTurnSum = new long[Constant.STATE_NUM];	//一定時間ごとの暇or忙しい1人あたりのエージェントリソースの合計
	private static double[] stateResources = new double[Constant.STATE_NUM];	//10tickごとに用いる状態ごとの1人あたりのリソース平均
	
	public static PrintWriter teamNumHistogramWriter;	//ヒストグラム用のデータを書き込むPrintWriter
	
	/**
	 * エージェントを返す
	 * @param i
	 * @return
	 */
	public static Agent getAgent(int i){
		return agents.get(i);
	}
	
	/**
	 * エージェントを返す
	 * @return
	 */
	public static List<Agent> getAgent(){
		return agents;
	}
	
	/**
	 * エージェントの平均能力を加算していく
	 * @param agent
	 */
	public static void addAgentAbility(Agent agent){
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			agentAbilityAverage[i] += agent.getAbility()[i];
		}
	}
	
	/**
	 * エージェントの平均能力を算出
	 * @return
	 */
	public static double[] getAgentAbilityAverage(){
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			agentAbilityAverage[i] = agentAbilityAverage[i] / (double)Constant.AGENT_NUM;
		}
		return agentAbilityAverage;
	}
	
	/**
	 * 初期状態のエージェントを返す
	 * @return
	 */
	public static List<Agent> getInitialAgent(){
		return initialAgents;
	}
	
	/**
	 * 初期状態のエージェントを返す
	 * @param i
	 * @return
	 */
	public static Agent getInitialAgent(int i){
		return initialAgents.get(i);
	}
	
	/**
	 * 初期状態でタスクをマークしたエージェントを入れる
	 * @param agent
	 */
	public static void addMarkingTaskAgent(Agent agent){
		markingTaskAgents.add(agent);
	}
	
	/**
	 * 初期状態でタスクをマークしなかったエージェントを入れる
	 * @param agent
	 */
	public static void addNotMarkingTaskAgent(Agent agent){
		notMarkingTaskAgents.add(agent);
	}
	
	/**
	 * タスクキューを参照する
	 * @return
	 */
	public static List<Task> lookingTaskQueue(){
		return taskQueue;
	}
	
	/**
	 * デッドラインによってタスクキューを昇順にソートする
	 */
	public static void sortTaskQueueByDeadline(){
		Collections.shuffle(taskQueue, Administrator.deadline_sort_random);
		Collections.sort(taskQueue, new Comparator<Task>(){
			public int compare(Task t1, Task t2){
				return t1.getDeadlineInTask() - t2.getDeadlineInTask();
			}
		});
	}
	
	/**
	 * リソース量によってタスクキューを降順にソートする
	 */
	public static void sortTaskQueueByRequire(){
		Collections.shuffle(taskQueue, Administrator.require_sort_random);
		Collections.sort(taskQueue, new Comparator<Task>(){
			public int compare(Task t1, Task t2){
				return t1.getTaskRequireSum() - t2.getTaskRequireSum();
			}
		});
	}
	
	/**
	 * タスクキューからタスクを取り出す
	 * @param task
	 */
	public static void getTask(Task task){
		taskQueue.remove(task);
	}
	
	/**
	 * マークのついていない、タスクキューのサイズを返す
	 * @return
	 */
	public static int getNoMarkTaskQueueSize(){
		int noMarkSize = 0;
		for(Task task : taskQueue){
			if(!task.getMark()){
				noMarkSize++;
			}
		}
		
		return noMarkSize;
	}
	
	/**
	 * 処理に成功したタスクリソース量を増やす
	 * @param successResource
	 */
	public static void addSuccess(int successResource){
		successTaskRequire += successResource;
		allSuccessTaskRequire += successResource;
		successTeamingCount++;
		allSuccessTeamingCount++;
	}
	
	/**
	 * 再割り当てを行ってチーム編成が成功した回数をカウント
	 */
	public static void addSuccessAgainAllocation(){
		successTeamingCountAgainAllocation++;
	}
	
	/**
	 * リーダとメンバのエッジ数をカウント
	 * @param memberNum
	 */
	public static void addSuccessTeamingEdgeToAgent(int memberNum){
		for(int i = 0; i < memberNum; i++){
			successTeamingEdgeForVisualization++;
		}
	}
	
	/**
	 * 廃棄するタスクリソース量を増やす
	 * @param failureResource
	 */
	public static void addFailureResource(int failureResource){
		failureTaskRequire += failureResource;
		allFailureTaskRequire += failureResource;
		failureTaskNum++;
	}
	
	/**
	 * 総タスク処理リソース量を返す
	 * @return
	 */
	public static int getAllSuccessTaskRequire(){
		return allSuccessTaskRequire;
	}
	
	/**
	 * 最後の数ターンのチーム編成成功回数をカウント
	 */
	public static void addSuccessTeamingCountAtEnd(){
		successTeamingCountAtEnd++;
	}
	
	/**
	 * 総タスク廃棄リソース量を返す
	 * @return
	 */
	public static int getAllFailureTaskRequire(){
		return allFailureTaskRequire;
	}
	
	/**
	 * チーム編成の失敗数を増やす
	 */
	public static void addFailureTeaming(){
		failureTeamingCount++;
	}
	
	/**
	 * チーム編成を諦めた回数を増やす
	 */
	public static void addGiveUpTeaming(){
		giveUpTeamingCount++;
	}
	
	/**
	 * チーム編成回数を増やす
	 */
	public static void addTeaming(){
		teamingCount++;
	}
	
	/**
	 * 1チームにおける拘束時間を数える
	 * @param time
	 */
	public static void addConstraintTimeInATeam(double time){
		constraintTimeInATeam += time;
	}
	
	/**
	 * 1チームにおける1人あたりの拘束時間を数える
	 * @param time
	 */
	public static void addConstraintTimePerAgentInATeam(double time){
		constraintTimePerAgentInATeam += time;
	}
	
	/**
	 * 1人あたりの拘束時間をカウント
	 * @param time
	 */
	public static void addConstraintTimePerAgent(double time){
		constraintTimePerAgent += time;
	}
	
	/**
	 * 最後の数ターンの処理していた時間をカウント
	 * @param time
	 */
	public static void addExecutingTimePerAgentInATeamAtEnd(double time){
		executingTimePerAgentInATeamAtEnd += time;
	}
	
	/**
	 * 最後の数ターンの処理していた時間を返す
	 * @return
	 */
	public static double getExecutingTimePerAgentInATeamAtEnd(){
		return executingTimePerAgentInATeamAtEnd;
	}
	
	/**
	 * 最後の数ターンの拘束されていた時間をカウント
	 * @param time
	 */
	public static void addConstraintTimePerAgentInATeamAtEnd(double time){
		constraintTimePerAgentInATeamAtEnd += time;
	}
	
	/**
	 * 最後の数ターンの拘束されていた時間を返す
	 * @return
	 */
	public static double getConstraintTimePerAentInATeamAtEnd(){
		return constraintTimePerAgentInATeamAtEnd;
	}
	
	/**
	 * 仮チームのサイズ合計を増やす
	 * （後で平均サイズを算出するため）
	 * @param temporaryTeamSize
	 */
	public static void addTemporaryTeamSize(int temporaryTeamSize){
		temporaryTeamSizeSum += temporaryTeamSize;
	}
	
	/**
	 * 仮チームの平均サイズを返す
	 * @return
	 */
	public static double getTemporaryTeamSizeAverage(){
		return (double)temporaryTeamSizeSum / (double)allSuccessTeamingCount;
	}
	
	/**
	 * チームサイズの合計を増やす
	 * （後で平均サイズを算出するため）
	 * @param teamSize
	 */
	public static void addTeamSize(int teamSize){
		teamSizeSum += teamSize;
	}
	
	/**
	 * チームの平均サイズを返す
	 * @return
	 */
	public static double getTeamSizeAverage(){
		return (double)teamSizeSum / (double)allSuccessTeamingCount;
	}
	
	/**
	 * 主にリーダを担当したエージェント数を返す
	 * Leader > Member × 2 のエージェント
	 * @return
	 */
	public static int getLeaderMain(){
		return leaderMain;
	}
	
	/**
	 * 主にメンバを担当したエージェント数を返す
	 * Member > Leader × 2 のエージェント
	 * @return
	 */
	public static int getMemberMain(){
		return memberMain;
	}
	
	/**
	 * 主に担当した役割がリーダでもメンバでもないエージェント数を返す
	 * @return
	 */
	public static int getNeitherLeaderNorMember(){
		return neitherLeaderNorMember;
	}
	
	/**
	 * チームのタスク処理時間を増やす
	 * （後で1チームあたりの平均タスク処理時間を算出するため）
	 * @param teamExecutingTime
	 */
	public static void addTeamExecuteTime(int teamExecutingTime){
		teamExecuteTimeSum += teamExecutingTime;
	}
	
	/**
	 * チームのタスク時間の平均を返す
	 * @return
	 */
	public static double getTeamExecuteTimeAverage(){
		return (double)teamExecuteTimeSum / (double)allSuccessTeamingCount;
	}
	
	/**
	 * 各状態においてかかった時間を集計（全エージェント値を合計）
	 * 平均を求めるため
	 * @param agent
	 */
	public static void addEachStateTime(Agent agent){
		initialStateTimeSum += agent.getInitialStateTime();
		leaderStateTimeSum += agent.getLeaderStateTime();
		memberStateTimeSum += agent.getMemberStateTime();
		executeStateTimeSum += agent.getExecuteStateTime();
		if(agent.getLeaderNum() + agent.getMemberNum() != 0){
			executeStateTimeOnceTime += (double)agent.getExecuteStateTime() / (double)(agent.getLeaderNum() + agent.getMemberNum());
			executeStateExecutingTimeSum += (double)(agent.getExecuteStateTime() - agent.getExecuteStateConstraintTime()) / (double)(agent.getLeaderNum() + agent.getMemberNum());
			executeStateConstraintTimeSum += (double)agent.getExecuteStateConstraintTime() / (double)(agent.getLeaderNum() + agent.getMemberNum());
		}
		if(agent.getTeamingSuccessAtEnd() != 0){
			executeStateExecutingTimeAtEndSum += (double)agent.getExecuteStateExecutingTimeAtEnd() / (double)agent.getTeamingSuccessAtEnd();
			executeStateConstraintTimeAtEndSum += (double)agent.getExecuteStateConstraintTimeAtEnd() / (double)agent.getTeamingSuccessAtEnd();
		}
	}
	
	/**
	 * 1人あたりの初期状態にかかる時間平均を返す
	 * @return
	 */
	public static double getInitialStateTimeAverage(){
		return initialStateTimeSum / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * 1人あたりのリーダ状態にかかる時間平均を返す
	 * @return
	 */
	public static double getLeaderStateTimeAverage(){
		return leaderStateTimeSum / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * 1人あたりのメンバ状態にかかる時間平均を返す
	 * @return
	 */
	public static double getMemberStateTimeAverage(){
		return memberStateTimeSum / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * 1人あたりの実行状態にかかる時間平均を返す
	 * @return
	 */
	public static double getExecuteStateTimeAverage(){
		return executeStateTimeSum / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * 1人あたりの1回の実行状態にかかる時間平均を返す
	 * @return
	 */
	public static double getExecuteStateTimeOnceTimeAverage(){
		return executeStateTimeOnceTime / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * 1人あたりの1回のチーム編成における実行状態において処理していた時間平均を返す
	 * @return
	 */
	public static double getExecuteStateExecutingTimeAverage(){
		return executeStateExecutingTimeSum / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * 1人あたりの1回のチーム編成における実行状態において無駄に拘束されている時間平均を返す
	 * @return
	 */
	public static double getExecuteStateConstraintTimeAverage(){
		return executeStateConstraintTimeSum / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * 最後の数ターンの処理していた時間平均を返す
	 * @return
	 */
	public static double getExecuteStateExecutingTimeAtEndAverage(){
		return executeStateExecutingTimeAtEndSum / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * 最後の数ターンの拘束されていた時間平均を返す
	 * @return
	 */
	public static double getExecuteStateConstraintTimeAtEndAverage(){
		return executeStateConstraintTimeAtEndSum / (double)Constant.AGENT_NUM;
	}
	
	/**
	 * ポアソン分布によるタスク追加数
	 * @param lambda
	 * @return
	 */
	public static int poissonAddTaskNum(double lambda){
		double xp = Administrator.poisson_random.nextDouble();
		int k = 0;
		while(xp >= Math.exp(-lambda)){
			xp = xp * Administrator.poisson_random2.nextDouble();
			k++;
		}
		
		return k;
	}
	
	/**
	 * 各状態にいるエージェント数を返す
	 * 初期状態、リーダorメンバ、実行状態
	 * @return
	 */
	private static int[] getEachStateAgentsNum(){
		int[] state = new int[Constant.STATE_NUM];
		Arrays.fill(state, 0);
		for(Agent agent : agents){
			if(agent.getNowState() == InitialMarkingTaskState.getInstance()){
				state[Constant.FREE_STATE]++;
				stateResources[Constant.FREE_STATE] += agent.getAbilitySum();
			}
			else if(agent.getNowState() == LeaderState.getInstance() || agent.getNowState() == MemberState.getInstance()){
				state[Constant.BUSY_LEADER_OR_MEMBER_STATE]++;
				stateResources[Constant.BUSY_LEADER_OR_MEMBER_STATE] += agent.getAbilitySum();
			}
			else if(agent.getNowState() == ExecuteState.getInstance()){
				state[Constant.BUSY_WORK_STATE]++;
				stateResources[Constant.BUSY_WORK_STATE] += agent.getAbilitySum();
			}
//			else{
//				System.out.println("あなたの状態は" + agent.getNowState() + "です");
//			}
		}
		
		return state;
	}
	
	/**
	 * 状態ごとの1人あたりのエージェントリソースを返す
	 * @param stateAgentNum
	 * @param turn
	 */
	private static void calculateEachStateAgentResources(int[] stateAgentNum, int turn){
//		int agentNum = 0;
		for(int state = 0; state < Constant.STATE_NUM; state++){
			if(stateAgentNum[state] == 0) stateResources[state] = 0;
			else stateResources[state] /= (double)stateAgentNum[state];
//			agentNum += stateAgentNum[state];
		}
//		if(agentNum != Constant.AGENT_NUM){
//			System.out.println("ターン数 = " + turn);
//			System.err.println("エージェント数が全体で" + Constant.AGENT_NUM +"体いません");
//			System.exit(1);
//		}
	}
	
	/**
	 * ヒストグラム用のファイルを開く
	 * @throws IOException
	 */
	public static void openTeamNumHistogramFile() throws IOException{
		teamNumHistogramWriter = FileWriter.teamNumHistogramFirst();
	}
	
	/********************************************************************************************************************/
	/**
	 * チーム編成を行う
	 * @param experimentNumber 実験回数
	 */
	public static void teamFormation(int experimentNumber) {
		
//		System.out.println("タスクリソース最小公倍数 = " + Constant.taskRequireLcm());
		
		/* エージェントの生成 */
//		System.out.println("エージェントを生成します");
		agents.clear();	//エージェントリストをクリア
		int agentId = 0;	//エージェント通し番号
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			int[] ability = new int[Constant.RESOURCE_NUM];
			for(int j = 0; j < Constant.RESOURCE_NUM; j++){
				ability[j] = Administrator.agent_random.nextInt(Constant.AGENT_ABILITY_MAX) + Constant.AGENT_ABILITY_INIT;
			}
//			agents.add(new LearningExpectedRewardPerTurnAgent1(agentId, ability));	//(1tickごとの獲得報酬 / 1tickごとの提示報酬)で報酬期待度を学習
			agents.add(new LearningExpectedRewardPerTurnAgent2(agentId, ability));	//1tickごとの獲得報酬で報酬期待度を学習
//			agents.add(new LearningExpectedRewardPerTurnAgent3(agentId, ability));	//(サブタスク処理時間 / チーム処理時間)で報酬期待度を学習
			agentId++;
		}
		
		/* エージェントの能力を加算 */
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			agentAbilityAverage[i] = 0;	//初期化
		}
		for(Agent agent : agents){
			addAgentAbility(agent);
		}
		
		/* エージェントの自己紹介 */
//		System.out.println("エージェントの自己紹介です");
//		for(Agent agent : agents){
//			agent.hello();
//		}
		
		/* タスクキューのクリア */
		taskQueue.clear();
		
		/* 処理量の初期化 */
		successTaskRequire = 0;
		allSuccessTaskRequire = 0;
		failureTaskRequire = 0;
		allFailureTaskRequire = 0;
		failureTaskNum = 0;
		successTeamingCount = 0;
		allSuccessTeamingCount = 0;
		successTeamingCountAtEnd = 0;
		successTeamingCountAgainAllocation = 0;
		failureTeamingCount = 0;
		giveUpTeamingCount = 0;
		teamingCount = 0;
		constraintTimeInATeam = 0;
		constraintTimePerAgentInATeam = 0;
		temporaryTeamSizeSum = 0;
		teamSizeSum = 0;
		leaderMain = 0;
		memberMain = 0;
		neitherLeaderNorMember = 0;
		teamExecuteTimeSum = 0;
		initialStateTimeSum = 0.0;
		leaderStateTimeSum = 0.0;
		memberStateTimeSum = 0.0;
		executeStateTimeSum = 0.0;
		executeStateTimeOnceTime = 0.0;
		executeStateExecutingTimeSum = 0.0;
		executeStateConstraintTimeSum = 0.0;
		executeStateExecutingTimeAtEndSum = 0.0;
		executeStateConstraintTimeAtEndSum = 0.0;
		executingTimePerAgentInATeamAtEnd = 0.0;
		constraintTimePerAgentInATeamAtEnd = 0.0;
		Arrays.fill(stateAgentNumPerTurnAgerage, 0.0);
		Arrays.fill(stateAgentNumPerTurnSum, 0);
		Arrays.fill(stateAgentResourcesPerTurnAverage, 0.0);
		Arrays.fill(stateAgentResourcesPerTurnSum, 0);
		Arrays.fill(stateResources, 0.0);
		
		try{
			/* ファイルの説明 */
			PrintWriter fileExplain = FileWriter.fileExplain();
			
			/* 欲張り度をcsvファイルに書き込み */
			PrintWriter greedyWriter = FileWriter.greedyFirst(agents, experimentNumber);
			
			/* リーダ、メンバの役割回数をcsvファイルに書き込み */
			PrintWriter roleNumberWriter = FileWriter.roleNumberFirst(agents, experimentNumber);
			
			/* チーム平均リソースをcsvファイルに書き込み */
			PrintWriter teamResourceAverageWriter = FileWriter.teamResourceAverageFirst(agents, experimentNumber);
			
			/* タスクキューをcsvファイルに書き込み */
			PrintWriter taskQueueWriter = FileWriter.taskQueueFirst(experimentNumber);
			
			/* 各状態にいるエージェント数を書き込み */
//			PrintWriter agentStateNumWriter = FileWriter.agentStateNumFirst(experimentNumber);
			
			/* ヒストグラム用のデータを書き込みするファイルを開く */
			openTeamNumHistogramFile();
			
			int measureTurn = Constant.MEASURE_Q_INIT_TURN_NUM;	//Q値の計測ターン
			int measureIntervalTurn = Constant.MEASURE_Q_TURN_NUM;	//Q値の計測間隔ターン
			int taskId = 0;	//タスク通し番号
			
			/* 処理を開始 */
			for(int turn = 1; turn <= Constant.TURN_NUM; turn++){
//				if(turn % 10000 == 0){
//					System.out.println(turn + "ターン目");
//				}
				
				//タスクを一定数に保つ
//				int addTaskNum = Constant.ALWAYS_TASK_NUM - getNoMarkTaskQueueSize();
				
				//前半と後半でタスク数を変える
//				int addTaskNum;
//				if(turn <= Constant.TURN_NUM/2){
//					addTaskNum = poissonAddTaskNum(Constant.ADD_TASK_PER_TURN);
//				}
//				else{
//					addTaskNum = poissonAddTaskNum(Constant.ADD_TASK_PER_TURN_LATTER);
//				}
				if(turn % Constant.WAIT_TURN == 1){
					/* キューにタスクを追加 */
					int addTaskNum = poissonAddTaskNum(Constant.ADD_TASK_PER_TURN);
//					System.out.println(addTaskNum + "個のタスクを追加しました");
					for(int i = 0; i < addTaskNum; i++){
						taskQueue.add(new Task(taskId, Administrator.task_random.nextInt(Constant.SUBTASK_IN_TASK_NUM) + Constant.SUBTASK_IN_TASK_INIT, Constant.WAIT_TURN * (Administrator.deadline_random.nextInt(Constant.DEADLINE_MAX) + Constant.DEADLINE_INIT)));	//1タスク中のサブタスクの数は3~5の中からランダムに決定

//						前半と後半でタスクのデッドラインを変える
//						if(turn <= Constant.TURN_NUM/2){
//							taskQueue.add(new Task(taskId, Administrator.task_random.nextInt(Constant.SUBTASK_IN_TASK_NUM) + Constant.SUBTASK_IN_TASK_INIT, Administrator.deadline_random.nextInt(Constant.DEADLINE_MAX) + Constant.DEADLINE_INIT));	//1タスク中のサブタスクの数は3~5の中からランダムに決定
//						}
//						else{
//							taskQueue.add(new Task(taskId, Administrator.task_random.nextInt(Constant.SUBTASK_IN_TASK_NUM) + Constant.SUBTASK_IN_TASK_INIT, Administrator.deadline_random.nextInt(Constant.DEADLINE_MAX_LATTER) + Constant.DEADLINE_INIT_LATTER));	//1タスク中のサブタスクの数は3~5の中からランダムに決定
//						}
						taskId++;
					}
				}
				
				/* タスクキューを表示 */
//				System.out.print("タスクキュー： id = ");
//				for(Task task : taskQueue){
//					if(!task.getMark()){
//						System.out.print(task.getId() + " ");
//					}
//				}
//				System.out.println();
				
				/* エージェントリストのクリア */
				initialAgents.clear();
				markingTaskAgents.clear();
				notMarkingTaskAgents.clear();
				initialWaitAgents.clear();
				leaderAgents.clear();
				leaderWaitAgents.clear();
				memberAgents.clear();
				memberWaitAgents.clear();
				executingAgents.clear();
				
				/* エージェントを状態ごとに分類 */
				for(Agent agent : agents){
					if(agent.getNowState() == InitialMarkingTaskState.getInstance()){
						initialAgents.add(agent);
					}
					else if(agent.getNowState() == InitialWaitState.getInstance()){
						initialWaitAgents.add(agent);
					}
					else if(agent.getNowState() == LeaderState.getInstance()){
						leaderAgents.add(agent);
					}
					else if(agent.getNowState() == LeaderWaitState.getInstance()){
						leaderWaitAgents.add(agent);
					}
					else if(agent.getNowState() == MemberState.getInstance()){
						memberAgents.add(agent);
					}
					else if(agent.getNowState() == MemberWaitState.getInstance()){
						memberWaitAgents.add(agent);
					}
					else if(agent.getNowState() == ExecuteState.getInstance()){
						executingAgents.add(agent);
					}
				}
				/* リストをランダムにシャッフル */
				Collections.shuffle(initialAgents, Administrator.initial_random);
				Collections.shuffle(initialWaitAgents, Administrator.initial_wait_random);
				Collections.shuffle(leaderAgents, Administrator.leader_random);
				Collections.shuffle(leaderWaitAgents, Administrator.leader_wait_random);
				Collections.shuffle(memberAgents, Administrator.member_random);
				Collections.shuffle(memberWaitAgents, Administrator.member_wait_random);
				Collections.shuffle(executingAgents, Administrator.executing_random);
				
				/* エージェントが実際に行動 */
				/* 初期待機 */
				for(Agent initialWait : initialWaitAgents){
//					System.out.println("初期待機状態のエージェントの行動");
					initialWait.action();
				}
				
				/* 初期状態 */
				for(Agent initial : initialAgents){
//					System.out.println("初期状態のエージェントの初期化");
					initial.initialize();
				}
				for(Agent initial : initialAgents){
//					System.out.println("初期タスク選択状態のエージェントの行動");
					initial.action();
				}
				for(Agent initial : markingTaskAgents){
//					System.out.println("初期役割選択状態のタスクをマークしたエージェントの行動");
					initial.action();
				}
				for(Agent initial : notMarkingTaskAgents){
//					System.out.println("初期役割選択状態のタスクをマークしなかったエージェントの行動");
					initial.action();
				}
				
				/* リーダ待機 */
				for(Agent leaderWait : leaderWaitAgents){
//					System.out.println("リーダ待機状態のエージェントの行動");
					leaderWait.action();
				}
				
				/* リーダ状態 */
				for(Agent leader : leaderAgents){
//					System.out.println("リーダ提案送信状態のエージェントの行動");
					leader.action();
				}
				
				/* メンバ待機 */
				for(Agent memberWait : memberWaitAgents){
//					System.out.println("メンバ待機状態のエージェントの行動");
					memberWait.action();
				}
				
				/* メンバ状態 */
				for(Agent member : memberAgents){
//					System.out.println("メンバ提案受託状態のエージェントの行動");
					member.action();
				}
				
				/* タスク実行状態 */
				for(Agent executing : executingAgents){
//					System.out.println("タスク実行状態のエージェントの行動");
					executing.action();
				}
				
				/* 経過ターン、100ターン毎のタスク処理数、総タスク処理数を書き込み */
				if(turn % Constant.MEASURE_TURN_NUM == 0){
					
					/* チーム拘束時間を1チームあたりの値で算出 */
					if(successTeamingCount != 0){
						constraintTimeInATeam /= (double)successTeamingCount;	
						constraintTimePerAgentInATeam /= (double)successTeamingCount;
					}
					
					/* エージェントごとの不要な平均拘束時間を求める */
					for(Agent agent : agents){
						if(agent.getTeamingSuccessNum() != 0){
							constraintTimePerAgent += (double)agent.getConstraintTimeSumPerAgent() / (double)agent.getTeamingSuccessNum();
						}
						agent.initializeDataPerTime();
					}
					constraintTimePerAgent /= Constant.AGENT_NUM;
					
					/* タスク処理リソース量を計測する */
					MeasureDataPerTime.measureRequireSum(turn/Constant.MEASURE_TURN_NUM - 1, successTaskRequire
							, failureTaskRequire, failureTaskNum, successTeamingCount, failureTeamingCount, giveUpTeamingCount
							, teamingCount, constraintTimeInATeam, constraintTimePerAgentInATeam, constraintTimePerAgent
							, successTeamingCountAgainAllocation);
					
					/* チーム人数ごとのチーム編成成功回数を計測する */
					MeasureConstraintTimeData.measureTeamingSuccess(turn/Constant.MEASURE_TURN_NUM - 1);
					
					/* タスクキューを書き込み */
					FileWriter.taskQueueWrite(taskQueueWriter, turn, taskQueue);
					
					/* チーム平均リソースを書き込み */
					FileWriter.teamResourceAverageWrite(teamResourceAverageWriter, turn, agents);
					
//					System.out.println("経過ターン：" + turn);
//					System.out.println(Constant.MEASURE_TURN_NUM + "ターンごとのタスク処理リソース量；" + successTaskRequire );
//					System.out.println("総タスク処理リソース量：" + allSuccessTaskRequire);
//					System.out.println(Constant.MEASURE_TURN_NUM + "ターンごとの廃棄タスク処理リソース量；" + failureTaskRequire );
//					System.out.println("総廃棄タスク処理リソース量：" + allFailureTaskRequire);
//					System.out.println();
					
					successTaskRequire = 0;		//25ターン毎のタスク処理リソース量の初期化
					failureTaskRequire = 0;		//25ターン毎の廃棄タスク処理リソース量の初期化
					failureTaskNum = 0;			//廃棄タスク数の初期化
					successTeamingCount = 0;	//25ターン毎のチーム編成成功回数の初期化
					failureTeamingCount = 0;	//25ターン毎のチーム編成失敗回数の初期化
					giveUpTeamingCount = 0;		//チーム編成を諦めた回数の初期化
					teamingCount = 0;			//チーム編成合計回数の初期化
					constraintTimeInATeam = 0;			//1チームあたりの拘束時間の初期化
					constraintTimePerAgentInATeam = 0;	//1チームにおける1人あたりの拘束時間の初期化
					constraintTimePerAgent = 0;	//1人あたりの拘束時間の初期化
					successTeamingCountAgainAllocation = 0;	//再割り当てを行ってチーム編成が成功した回数の初期化
				}
				
				
				if(turn == measureTurn){
					/* 欲張り度の書き込み */
					FileWriter.greedyWrite(greedyWriter, turn, agents);

					/* 提案受託期待度の書き込み */
					FileWriter.trustWrite(agents, turn, experimentNumber);

					/* 報酬期待度の書き込み */
					FileWriter.expectedRewardWrite(agents, turn, experimentNumber);
					
//					if(measureTurn / 10 == measureIntervalTurn){
//						measureIntervalTurn *= 10; 
//					}
					
					measureTurn += measureIntervalTurn;
				}
				
				if(turn % Constant.MEASURE_STATE_TURN_NUM == 0){
					/* 暇or忙しいエージェント数を配列に */
					int[] stateAgentNum = getEachStateAgentsNum();
					/* 暇or忙しいエージェントの1人あたりのリソースを計算 */
					calculateEachStateAgentResources(stateAgentNum, turn);
					
					/* 暇or忙しいエージェント数を書き込み */
//					FileWriter.agentStateNumWrite(agentStateNumWriter, turn, getNoMarkTaskQueueSize(), stateAgentNum);
					
					/* 一定時間ごとに暇or忙しいエージェント数を加算 */
					for(int i = 0; i < stateAgentNum.length; i++){
						stateAgentNumPerTurnSum[i] += stateAgentNum[i];
						stateAgentResourcesPerTurnSum[i] += stateResources[i];
						stateResources[i] = 0.0;
					}
				}
				
				if(turn % Constant.MEASURE_VISUALIZATION_TURN_NUM == 0){
					/* 可視化のデータの書き込み */
					FileWriter.visualizationDataWrite(agents, turn, experimentNumber, successTeamingEdgeForVisualization);
					FileWriter.visualizationTeamingDataWrite(agents, turn);
					/* データの初期化 */
					for(Agent agent : agents){
						agent.initializeTeamingWithLeaderAndMemberPerTime();
					}
					successTeamingEdgeForVisualization = 0;
				}
				
				/* タスクのデッドラインを減らす */
				for(int i = 0; i < taskQueue.size(); ){
					taskQueue.get(i).subtractDeadlineInTask();
					/* タスクのデッドラインが2以下になったらキューから削除する */
					if(taskQueue.get(i).getDeadlineInTask() <= 2 && taskQueue.get(i).getMark() == false){
						addFailureResource(taskQueue.get(i).getTaskRequireSum());	//廃棄リソースを数え上げる
						taskQueue.remove(taskQueue.get(i));	//キューからタスクを削除
					}
					else i++;
				}
				
//				for(int i = 0; i < 200; i++){
//					System.out.print("-");
//				}
//				System.out.println();
				
			}
			/* リーダ、メンバの役割回数、チーム編成回数を書き込み */
			FileWriter.roleNumberWrite(roleNumberWriter, agents);
			FileWriter.teamingNumWrite(agents);
			
			/* fileExplainのファイルを閉じる */
			fileExplain.println();
			fileExplain.close();

			/* greedyWriterのファイルを閉じる */
			greedyWriter.println();
			greedyWriter.close();
			
			/* roleNumberWriterのファイルを閉じる */
			roleNumberWriter.println();
			roleNumberWriter.close();
			
			/* teamResourceAverageWriterのファイルを閉じる */
			teamResourceAverageWriter.println();
			teamResourceAverageWriter.close();
			
			/* taskQueueWriterのファイルを閉じる */
			taskQueueWriter.println();
			taskQueueWriter.close();
			
			/* agentStateNumWriterのファイルを閉じる */
//			agentStateNumWriter.println();
//			agentStateNumWriter.close();
			
			/* teamNumHistogramWriterのファイルを閉じる */
			teamNumHistogramWriter.println();
			teamNumHistogramWriter.close();
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		/* エージェントの主に担当した役割の回数を保持 */
		for(Agent agent : agents){
			if(agent.getLeaderNum() > agent.getMemberNum() * 2){
				leaderMain++;
			}
			else if(agent.getMemberNum() > agent.getLeaderNum() * 2){
				memberMain++;
			}
			else{
				neitherLeaderNorMember++;
			}
		}
		
		/* 最後の数ターンの1チーム中の1人あたりの処理・拘束時間を計測 */
		if(successTeamingCountAtEnd != 0){
			executingTimePerAgentInATeamAtEnd /= (double)successTeamingCountAtEnd;
			constraintTimePerAgentInATeamAtEnd /= (double)successTeamingCountAtEnd;
		}
		
		/* 1回のチーム編成ごとのチーム拘束時間の差分を計算 */
		MeasureConstraintTimeData.calculateTeamConstraintTimeDifference();
		
		/* 1人あたりのエージェントの各状態にかけた時間を集計 */
		for(Agent agent : agents){
			addEachStateTime(agent);
		}
		
		/* 1回の実験の暇or忙しいエージェント数、1人あたりのリソースの平均を算出 */
		for(int i = 0; i < stateAgentNumPerTurnAgerage.length; i++){
			stateAgentNumPerTurnAgerage[i] = (double)stateAgentNumPerTurnSum[i] / (double)(Constant.TURN_NUM / Constant.MEASURE_STATE_TURN_NUM);
			stateAgentResourcesPerTurnAverage[i] = (double)stateAgentResourcesPerTurnSum[i] / (double)(Constant.TURN_NUM / Constant.MEASURE_STATE_TURN_NUM);
			stateResources[i] = 0;
		}
		Administrator.addStateAgentNumAndResouces(stateAgentNumPerTurnAgerage, stateAgentResourcesPerTurnAverage);
	}

}
