package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import log.Log;
import post.Post;
import state.RoleSelectionState;
import state.SubtaskAllocationState;
import state.SubtaskReceptionState;
import state.TaskExecuteState;
import state.TaskMarkedWaitingState;
import state.TaskSelectionState;
import constant.Constant;
import agent.Agent;

public class TeamFormationMain {
	
	private static int turn = 0;
	
	private static TeamFormationParameter parameter = new TeamFormationParameter();
	private static TeamFormationMeasuredData measure = new TeamFormationMeasuredData();
	private static Post post = new Post();
	
	
	public static TeamFormationParameter getParameter() {
		return parameter;
	}
	
	public static TeamFormationMeasuredData getMeasure() {
		return measure;
	}
	
	public static int getTurn() {
		return turn;
	}
	
	public static Post getPost() {
		return post;
	}
	
	private static void actionByMarkedWatingAgent() {
		// 自分に来ているメッセージを破棄
		for(Agent agent : parameter.agentsMap.get(TaskMarkedWaitingState.getState())){
			agent.getParameter().getOfferMessages().clear();
		}
		Log.log.debugln("------- タスクマーク待機状態のエージェントの行動 -------");
		for(Agent agent : parameter.agentsMap.get(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}
	
	private static void actionByInitialAgent() {
		for(Agent agent : parameter.agentsMap.get(TaskSelectionState.getState())){
			agent.getParameter().initialize();
		}
		Log.log.debugln("------- タスク選択状態のエージェントの行動 -------");
		for(Agent agent : parameter.agentsMap.get(TaskSelectionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}
	
	private static void actionByRoleSelectionAgent() {
		Log.log.debugln("------- 役割選択状態のエージェントの行動 / タスクをマークしていないエージェント -------");
		for(Agent agent : parameter.agentsMap.get(RoleSelectionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}
	
	private static void actionByLeaderOrMemberAgent() {
		Log.log.debugln("------- リーダ状態のエージェントの行動 -------");
		for(Agent agent : parameter.agentsMap.get(SubtaskAllocationState.getState())){
			agent.action();
		}
		Log.log.debugln();
		Log.log.debugln("------- メンバ状態のエージェントの行動 -------");
		for(Agent agent : parameter.agentsMap.get(SubtaskReceptionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}
	
	private static void actionByExecuteAgent() {
		Log.log.debugln("------- タスク実行状態のエージェントの行動 -------");
		for(Agent agent : parameter.agentsMap.get(TaskExecuteState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public static void teamFormation(int experimentNumber) throws IOException {
		parameter.initialize();
		measure.initialize();
		
		// エージェントの生成
		for(int id = 0; id < Constant.AGENT_NUM; id++){
			parameter.agents.add(new Agent(id));
			
		}
		// エージェントの能力を指定して生成
//		makeAgents();
		
		parameter.debugAgents();
		
		// ファイル書き込み用のPrintWriterインスタンスを取得
		PrintWriter greedyWriter = getGreedyWriter(experimentNumber);
		
		// タスクキュー書き込み用PrintWriterインスタンスを取得
		PrintWriter taskQueueWriter = getTaskQueueWriter(experimentNumber);
		
		// チーム編成の開始
		for(turn = 1; turn <= Constant.TURN_NUM; turn++){
			Log.log.debugln("======= " + turn + " ターン目 =======");
			
			// キューにタスクを追加
			// TODO ADD_TASK_INTERVAL=1のときはturn % Constant.ADD_TASK_INTERVAL == 0に変える必要性
			if(turn % Constant.ADD_TASK_INTERVAL == 0){
				parameter.addTaskToQueue();	
			}
			
			// agentsMapを空にする & taskMarkingAgentsMapを空にする
			parameter.clearAgentsMap();
			parameter.clearTaskMarkingAgentsMap();
			
			// 状態ごとにエージェントを分ける
			parameter.classifyAgentIntoState();
			
			// 状態ごとのエージェントマップをシャッフル
			parameter.shuffleAgentsMap();
			
			// 行動する
			actionByInitialAgent();
			actionByMarkedWatingAgent();
			actionByRoleSelectionAgent();
			actionByLeaderOrMemberAgent();
			actionByExecuteAgent();
			
			// タスクキューのサイズを計算
			int noMarkTaskNum = parameter.getNoMarkingTaskNum();
			measure.countTaskQueueNum(noMarkTaskNum, parameter.taskQueue.size());
			
			// デッドラインを減らす
			parameter.decreaseTaskDeadline(measure);
			
			// 一定時間ごとに、計測データを退避
			if(turn % Constant.MEASURE_TURN_NUM == 0){
				// 計測データの配列添え字をインクリメント
				measure.addArrayIndex();
			}
			
			// Q値をファイルに書き込み
			if(turn % Constant.MEASURE_Q_TURN_NUM == 0){
				writeMeasuredDataPerTurn(greedyWriter, turn, parameter.agents, experimentNumber);
			}
			
			// 可視化用計測データをファイルに書き込み
			if(turn % Constant.MEASURE_VISUALIZATION_TURN_NUM == 0){
				writeVisualData(parameter.agents, experimentNumber, measure.allSuccessTeamFormationEdge);
			}
			
			// タスクキューの中身を書き込み
			if(Constant.TURN_NUM - turn < Constant.END_TURN_NUM){
				writeTaskQueue(experimentNumber, taskQueueWriter, noMarkTaskNum);
			}
			
		}
		
		// チーム編成1回のみに必要なデータを計測
		writeMeasuredData(parameter.agents, experimentNumber);
		
		// 1回のチーム編成におけるエージェントごとのデータを計測
		measure.measureAtEnd(parameter.agents);
		
		// greedyWriterをclose
		closeGreedyWrite(experimentNumber, greedyWriter);
		
		// taskQueueWriterをclose
		closeTaskQueueWrite(experimentNumber, taskQueueWriter);
		
		debugExecutedTaskRequire();
	}
	
	private static void debugExecutedTaskRequire() {
		Log.log.debugln("処理したタスクリソース量 = " + measure.allSuccessTaskRequire);
		Log.log.debugln("処理したタスク数 = " + measure.allSuccessTeamFormationNum);
	}
	
	private static PrintWriter getGreedyWriter(int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			return FileWriteManager.writeHeaderOfGreedy(parameter.agents);
		}
		else{
			return null;
		}
	}
	
	private static void closeGreedyWrite(int experimentNumber, PrintWriter greedyWriter) {
		if(experimentNumber == 1){
			greedyWriter.close();
		}
	}
	
	private static void writeMeasuredData(ArrayList<Agent> agents, int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			FileWriteManager.writeBodyOfRoleNumber(agents);
			FileWriteManager.writeTeamFormationWithAgent(agents);
		}
	}
	
	private static void writeMeasuredDataPerTurn(PrintWriter greedy, int turn, ArrayList<Agent> agents, int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			FileWriteManager.writeBodyOfGreedy(greedy, turn, agents);
			FileWriteManager.writeTrustToMember(agents, turn);
			FileWriteManager.writeRewardExpectation(agents, turn);
		}
	}
	
	private static void writeVisualData(ArrayList<Agent> agents, int experimentNumber, int successTeamFormationEdge) throws IOException {
		if(experimentNumber == 1){
			VisualFileWriter.writeVisualizedData(agents, getTurn(), experimentNumber, successTeamFormationEdge);
			VisualFileWriter.writeVisualizedMoreData(agents, getTurn());
		}
	}
	
	private static PrintWriter getTaskQueueWriter(int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			return FileWriteManager.writeHeaderOfTaskQueue();
		}
		else{
			return null;
		}
	}
	
	private static void writeTaskQueue(int experimentNumber, PrintWriter taskQueueWriter, int noMarkTaskNum) throws IOException {
		if(experimentNumber == 1){
			FileWriteManager.writeBodyOfTaskQueue(taskQueueWriter, getTurn(), parameter.taskQueue
					, noMarkTaskNum);
		}
	}
	
	private static void closeTaskQueueWrite(int experimentNumber, PrintWriter taskQueueWriter) throws IOException {
		if(experimentNumber == 1){
			taskQueueWriter.close();
		}
	}
	
	private static void makeAgents() {
		for(int id = 0; id < Constant.AGENT_NUM; id++){
			int[] ability = new int[Constant.RESOURCE_NUM];
			for(int i = 0; i < ability.length; i++){
				ability[i] = id % Constant.AGENT_ABILITY_MAX + Constant.AGENT_ABILITY_INIT;
			}
			parameter.agents.add(new Agent(id, ability));
		}
	}

}
