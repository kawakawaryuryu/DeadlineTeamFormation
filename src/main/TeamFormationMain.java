package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import post.Post;
import state.RoleSelectionState;
import state.SubtaskAllocationState;
import state.SubtaskReceptionState;
import state.TaskExecuteState;
import state.TaskMarkedWaitingState;
import state.TaskSelectionState;
import constant.FixedConstant;
import agent.FixedAgent;

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
		for(FixedAgent agent : parameter.agentsMap.get(TaskMarkedWaitingState.getState())){
			agent.getParameter().getOfferMessages().clear();
		}
//		System.out.println("------- タスクマーク待機状態のエージェントの行動 -------");
		for(FixedAgent agent : parameter.agentsMap.get(TaskMarkedWaitingState.getState())){
			agent.action();
		}
//		System.out.println();
	}
	
	private static void actionByInitialAgent() {
		for(FixedAgent agent : parameter.agentsMap.get(TaskSelectionState.getState())){
			agent.getParameter().initialize();
		}
//		System.out.println("------- タスク選択状態のエージェントの行動 -------");
		for(FixedAgent agent : parameter.agentsMap.get(TaskSelectionState.getState())){
			agent.action();
		}
//		System.out.println();
	}
	
	private static void actionByRoleSelectionAgent() {
//		System.out.println("------- 役割選択状態のエージェントの行動 / タスクをマークしていないエージェント -------");
		for(FixedAgent agent : parameter.agentsMap.get(RoleSelectionState.getState())){
			agent.action();
		}
//		System.out.println();
	}
	
	private static void actionByLeaderOrMemberAgent() {
//		System.out.println("------- リーダ状態のエージェントの行動 -------");
		for(FixedAgent agent : parameter.agentsMap.get(SubtaskAllocationState.getState())){
			agent.action();
		}
//		System.out.println();
//		System.out.println("------- メンバ状態のエージェントの行動 -------");
		for(FixedAgent agent : parameter.agentsMap.get(SubtaskReceptionState.getState())){
			agent.action();
		}
//		System.out.println();
	}
	
	private static void actionByExecuteAgent() {
//		System.out.println("------- タスク実行状態のエージェントの行動 -------");
		for(FixedAgent agent : parameter.agentsMap.get(TaskExecuteState.getState())){
			agent.action();
		}
//		System.out.println();
	}

	public static void teamFormation(int experimentNumber) throws IOException {
		parameter.initialize();
		measure.initialize();
		
		// エージェントの生成
		for(int id = 0; id < FixedConstant.AGENT_NUM; id++){
			parameter.agents.add(new FixedAgent(id));
			
		}
		// エージェントの能力を指定して生成
//		makeAgents();
		
//		parameter.debugAgents();
		
		// ファイル書き込み用のPrintWriterインスタンスを取得
		PrintWriter greedyWriter = getGreedyWriter(experimentNumber);
		
		// タスクキュー書き込み用PrintWriterインスタンスを取得
		PrintWriter taskQueueWriter = getTaskQueueWriter(experimentNumber);
		
		// チーム編成の開始
		for(turn = 1; turn <= FixedConstant.TURN_NUM; turn++){
//			System.out.println("======= " + turn + " ターン目 =======");
			
			// キューにタスクを追加
			// TODO ADD_TASK_INTERVAL=1のときはturn % FixedConstant.ADD_TASK_INTERVAL == 0に変える必要性
			if(turn % FixedConstant.ADD_TASK_INTERVAL == 0){
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
			if(turn % FixedConstant.MEASURE_TURN_NUM == 0){
				// 計測データの配列添え字をインクリメント
				measure.addArrayIndex();
			}
			
			// Q値をファイルに書き込み
			if(turn % FixedConstant.MEASURE_Q_TURN_NUM == 0){
				writeMeasuredDataPerTurn(greedyWriter, turn, parameter.agents, experimentNumber);
			}
			
			// 可視化用計測データをファイルに書き込み
			if(turn % FixedConstant.MEASURE_VISUALIZATION_TURN_NUM == 0){
				writeVisualData(parameter.agents, experimentNumber, measure.allSuccessTeamFormationEdge);
			}
			
			// タスクキューの中身を書き込み
			if(FixedConstant.TURN_NUM - turn < FixedConstant.END_TURN_NUM){
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
		
//		debugExecutedTaskRequire();
	}
	
	private static void debugExecutedTaskRequire() {
		System.out.println("処理したタスクリソース量 = " + measure.allSuccessTaskRequire);
		System.out.println("処理したタスク数 = " + measure.allSuccessTeamFormationNum);
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
	
	private static void writeMeasuredData(ArrayList<FixedAgent> agents, int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			FileWriteManager.writeBodyOfRoleNumber(agents);
			FileWriteManager.writeTeamFormationWithAgent(agents);
		}
	}
	
	private static void writeMeasuredDataPerTurn(PrintWriter greedy, int turn, ArrayList<FixedAgent> agents, int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			FileWriteManager.writeBodyOfGreedy(greedy, turn, agents);
			FileWriteManager.writeTrust(agents, turn);
			FileWriteManager.writeRewardExpectation(agents, turn);
		}
	}
	
	private static void writeVisualData(ArrayList<FixedAgent> agents, int experimentNumber, int successTeamFormationEdge) throws IOException {
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
		for(int id = 0; id < FixedConstant.AGENT_NUM; id++){
			int[] ability = new int[FixedConstant.RESOURCE_NUM];
			for(int i = 0; i < ability.length; i++){
				ability[i] = id % FixedConstant.AGENT_ABILITY_MAX + FixedConstant.AGENT_ABILITY_INIT;
			}
			parameter.agents.add(new FixedAgent(id, ability));
		}
	}

}