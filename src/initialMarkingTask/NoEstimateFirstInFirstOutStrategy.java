package initialMarkingTask;

import main.Main;
import task.Task;
import agent.Agent;

/**
 * タスクをデッドラインまでに処理できるかどうか見積もらない
 */
public class NoEstimateFirstInFirstOutStrategy implements InitialMarkingTaskStrategy {

	/**
	 * キューからタスクを選ぶ
	 * （キューの先頭から選ぶ）
	 * @param agent
	 * @return
	 */
	@Override
	public Task selectTask(Agent agent){
		for(Task task : Main.lookingTaskQueue()){
			if(!task.getMark()){
				return task;
			}
		}
		return null;
	}
	
	/**
	 * チームでタスクをデッドラインまでの処理できるか見積もる
	 * @param agent
	 * @param task
	 * @return
	 */
	public boolean isExecutedTaskInTeam(Agent agent, Task task){
		return true;
	}
	
	/**
	 * キューソートの方法を返す
	 */
	public String toString(){
		return "FIFO + 見積もりなし";
	}

}
