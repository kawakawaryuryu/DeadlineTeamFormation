package main.teamformation;

import java.io.IOException;

import log.Log;
import config.Configuration;
import constant.Constant;
import agent.Agent;

public class TeamFormationMain {
	
	private static int turn = 0;
	
	public static int getTurn() {
		return turn;
	}
	
	private static TeamFormationInstances instance = TeamFormationInstances.getInstance();

	public static void teamFormation(int experimentNumber) throws IOException {
		instance.initialize();
		
		// エージェントの生成
		instance.getParameter().makeAgents();

		// エージェントの能力を指定して生成
//		makeAgents();
		
		instance.getParameter().debugAgents();
		
		
		// チーム編成の開始
		for(turn = 1; turn <= Constant.TURN_NUM; turn++){
			Log.log.debugln("======= " + turn + " ターン目 =======");
			
			// キューにタスクを追加
			// TODO ADD_TASK_INTERVAL=1のときはturn % Constant.ADD_TASK_INTERVAL == 0に変える必要性
			if(isTaskTurn(turn, Constant.ADD_TASK_INTERVAL)){
				instance.getParameter().addTaskToQueue();	
			}
			
			// agentsMapを空にする & taskMarkingAgentsMapを空にする
			instance.getParameter().clearAgentsMap();
			instance.getParameter().clearTaskMarkingAgentsMap();
			
			// 状態ごとにエージェントを分ける
			instance.getParameter().classifyAgentIntoState();
			
			// 状態ごとのエージェントマップをシャッフル
			instance.getParameter().shuffleAgentsMap();
			
			// 行動する
			Configuration.model.run(Configuration.action);
			
			// タスクキューのサイズを計算
			int noMarkTaskNum = instance.getParameter().getNoMarkingTaskNum();
			instance.getMeasure().countTaskQueueNum(noMarkTaskNum, instance.getParameter().taskQueue.size());
			
			// デッドラインを減らす
			instance.getParameter().decreaseTaskDeadline(instance.getMeasure());
			
			
		}
		
		// 1回のチーム編成におけるエージェントごとのデータを計測
		instance.getMeasure().measureAtEnd(instance.getParameter().agents);
		
		
		debugExecutedTaskRequire();
	}
	
	private static boolean isTaskTurn(int turn, int interval) {
		if (interval == 1) {
			return true;
		} else if (interval > 1) {
			return turn % interval == 1;
		} else {
			System.err.println("intervalの値がありえません");
			System.exit(-1);
			return false;
		}
	}
	
	private static void debugExecutedTaskRequire() {
		Log.log.debugln("処理したタスクリソース量 = " + instance.getMeasure().allSuccessTaskRequire);
		Log.log.debugln("処理したタスク数 = " + instance.getMeasure().allSuccessTeamFormationNum);
	}
	
	
	private static void makeAgents() {
		for(int id = 0; id < Constant.AGENT_NUM; id++){
			int[] ability = new int[Constant.RESOURCE_NUM];
			for(int i = 0; i < ability.length; i++){
				ability[i] = id % Constant.AGENT_ABILITY_MAX + Constant.AGENT_ABILITY_INIT;
			}
			instance.getParameter().agents.add(new Agent(id, ability));
		}
	}

}
