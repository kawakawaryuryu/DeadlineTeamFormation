package state;

import main.Constant;
import agent.Agent;

/**
 * タスク実行状態
 */
public class ExecuteState implements State {
	private static State onlyState = new ExecuteState();
	
	/**
	 * エージェントの行動
	 * @param agent
	 */
	@Override
	public void agentAction(Agent agent) {
		agent.addExecuteTimer();
		agent.addExecuteStateTime();
		if(agent.getExecuteTimer() > agent.getExecuteTime()){
			agent.addExecuteStateConstraintTime();	//無駄に拘束されている時間をカウント
		}
		
		/* 最後の数ターンの処理時間、拘束時間をカウント */
//		if(agent.getTurn() > Constant.TURN_NUM - Constant.MEASURE_SUCCESS_AT_END_TURN_NUM){
//			if(agent.getExecuteTimer() > agent.getExecuteTime()){
//				agent.addExecuteStateConstraintTimeAtEnd();	//無駄に拘束されている時間をカウント
//			}
//			else{
//				agent.addExecuteStateExecutingTimeAtEnd();	//処理時間をカウント
//			}
//		}
		
//		System.out.println("タスク実行状態 " + agent.getTimer() + "tick目");
//		System.out.println("自分のチーム：" + agent.getTeamInfo());
		
		/* チームの拘束時間になったら、初期状態に戻る */
//		System.out.println(agent + " の（" + agent.getRole() + "）チーム情報 " + agent.getTeamInfo());
		if(agent.getExecuteTimer() == agent.getTeamInfo().getExecuteTime()){
			recordTeam(agent, agent.getRole());	//チームを記録する
			agent.changeState(InitialMarkingTaskState.getInstance());
		}
		else{
			agent.changeState(ExecuteState.getInstance());
		}
	}
	
	/**
	 * リーダの場合、チームを記録する
	 * @param agent
	 * @param role リーダだったかメンバだったかを返す
	 */
	public void recordTeam(Agent agent, int role){
		/* リーダの場合 */
		if(role == Constant.LEADER_STATE){
//			Main.getTask(agent.getMarkingTask());	//マークしていたタスクを廃棄する（マークなし）
			agent.addToPastTeam(agent.getTeamInfo());	//チーム履歴に今のチームを追加
			agent.calculateAverageTeamResource();	//チーム履歴の平均リソースを求めておく	
			agent.updateAverageExecuteTimePerResource();	//1リソースあたりにかかる平均時間を更新
		}
	}
	
	/**
	 * 状態を返す
	 * @return
	 */
	public static State getInstance(){
		return onlyState;
	}
	
	/**
	 * 状態を表す
	 */
	public String toString(){
		return "タスク実行状態";
	}



}
