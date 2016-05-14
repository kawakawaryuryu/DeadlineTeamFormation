package main.teamformation;

import log.Log;
import main.file.FileManager;
import config.Configuration;
import constant.Constant;
import agent.Agent;

public class TeamFormationMain {
	
	private static int turn = 0;
	
	private static FileManager fileMng = new FileManager();
	
	public static int getTurn() {
		return turn;
	}
	

	public static void teamFormation(int experimentNumber) {
		TeamFormationInstances.getInstance().initialize();
		
		// エージェントの生成
		// TODO 全てをTeamFormationParameterに任してよいか（完全にブラックボックスでよいか）
		TeamFormationInstances.getInstance().getParameter().makeAgents();

		// エージェントの能力を指定して生成
//		makeAgents();
		
		TeamFormationInstances.getInstance().getParameter().debugAgents();
		
		// 書き込みファイルインスタンスの設定
		fileMng.setFileInstances(experimentNumber);
		
		// チーム編成の開始
		for(turn = 1; turn <= Constant.TURN_NUM; turn++){
			Log.log.debugln("======= " + turn + " ターン目 =======");
			
			// キューにタスクを追加
			// TODO ADD_TASK_INTERVAL=1のときはturn % Constant.ADD_TASK_INTERVAL == 0に変える必要性
			if(isTaskTurn(turn, Constant.ADD_TASK_INTERVAL)){
				TeamFormationInstances.getInstance().getParameter().addTaskToQueue();	
			}
			
			// agentsMapを空にする & taskMarkingAgentsMapを空にする
			TeamFormationInstances.getInstance().getParameter().clearAgentsMap();
			TeamFormationInstances.getInstance().getParameter().clearTaskMarkingAgentsMap();
			
			// 状態ごとにエージェントを分ける
			TeamFormationInstances.getInstance().getParameter().classifyAgentIntoState();
			
			// 状態ごとのエージェントマップをシャッフル
			TeamFormationInstances.getInstance().getParameter().shuffleAgentsMap();
			
			// 行動する
			// TODO modelはConfigurationから引っ張ってくるでいいか コンストラクタ引数で与える必要はないか
			Configuration.model.run(Configuration.action);
			
			// タスクキューのサイズを計算
			// TODO ここだけ系統の違う処理 別クラスに移す必要あるか
			int noMarkTaskNum = TeamFormationInstances.getInstance().getParameter().getNoMarkingTaskNum();
			TeamFormationInstances.getInstance().getMeasure().countTaskQueueNum(noMarkTaskNum, TeamFormationInstances.getInstance().getParameter().taskQueue.size());
			
			// デッドラインを減らす
			TeamFormationInstances.getInstance().getParameter().decreaseTaskDeadline(TeamFormationInstances.getInstance().getMeasure());
			
			// ファイルに計測データ書き込み
			fileMng.write(turn, experimentNumber, noMarkTaskNum);
			
		}
		// 1回のチーム編成に1回のみ書き込めばよいデータの書き込み
		fileMng.writeOnce(experimentNumber);
		
		// 1回のチーム編成におけるエージェントごとのデータを計測
		TeamFormationInstances.getInstance().getMeasure().measureAtEnd(TeamFormationInstances.getInstance().getParameter().agents);
		
		// ファイルインスタンスをclose
		fileMng.close(experimentNumber);
		
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
		Log.log.debugln("処理したタスクリソース量 = " + TeamFormationInstances.getInstance().getMeasure().allSuccessTaskRequire);
		Log.log.debugln("処理したタスク数 = " + TeamFormationInstances.getInstance().getMeasure().allSuccessTeamFormationNum);
	}
	
	
	private static void makeAgents() {
		for(int id = 0; id < Constant.AGENT_NUM; id++){
			int[] ability = new int[Constant.RESOURCE_NUM];
			for(int i = 0; i < ability.length; i++){
				ability[i] = id % Constant.AGENT_ABILITY_MAX + Constant.AGENT_ABILITY_INIT;
			}
			TeamFormationInstances.getInstance().getParameter().agents.add(new Agent(id, ability));
		}
	}

}
