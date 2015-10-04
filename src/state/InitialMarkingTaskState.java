package state;

import main.Main;
import initialMarkingTask.FirstInFirstOutStrategy;
import initialMarkingTask.EarlyDeadlineFirstStrategy;
import initialMarkingTask.MoreRequireFirstStrategy;
import initialMarkingTask.InitialMarkingTaskStrategy;
import initialMarkingTask.NoEstimateFirstInFirstOutStrategy;
import agent.Agent;
import task.Task;

/**
 * 初期タスク予約状態
 */
public class InitialMarkingTaskState implements State {
	
	private static State onlyState = new InitialMarkingTaskState();	//状態を保持する（singletonパターン）
	private InitialMarkingTaskStrategy strategy = new FirstInFirstOutStrategy();	//初期状態のメソッド群
//	private InitialMarkingTaskStrategy strategy = new NoEstimateFirstInFirstOutStrategy();	//FIFO,タスク処理を見積もらない
//	private InitialMarkingTaskStrategy strategy = new EarlyDeadlineFirstStrategy();	//初期状態のメソッド群
//	private InitialMarkingTaskStrategy strategy = new MoreRequireFirstStrategy();	//初期状態のメソッド群

	/**
	 * エージェントの行動
	 * キューを参照し、タスクをマーク
	 * キューにタスクあり→マーク
	 * キューにタスクなし→そのまま
	 */
	@Override
	public void agentAction(Agent agent) {
		
		Task selectedTask = strategy.selectTask(agent);
		if(selectedTask != null){
//			System.out.println(selectedTask + "をマークしました");
			agent.markingTask(selectedTask);	//タスクを保持
			Main.addMarkingTaskAgent(agent);
		}
		else{
//			System.out.println("タスクをマークしませんでした");
			Main.addNotMarkingTaskAgent(agent);
		}
		
		agent.changeState(InitialRoleSelectState.getInstance());
	}
	
	/**
	 * 状態インスタンスを返す
	 * @return
	 */
	public static State getInstance(){
		return onlyState;
	}
	
	/**
	 * タスクマークの際の戦略を返す
	 * @return
	 */
	public InitialMarkingTaskStrategy getMarkingTaskStrategy(){
		return strategy;
	}
	
	/**
	 * 状態を返す
	 */
	public String toString(){
		return "初期タスク予約状態";
	}

}
