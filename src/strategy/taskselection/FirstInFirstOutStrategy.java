package strategy.taskselection;

import task.Task;
import library.EstimationLibrary;
import main.teamformation.TeamFormationInstances;
import agent.Agent;

/**
 * キューはそのまま並び替えない
 * （タスク選択はキューの先頭から選ぶ）
 */
public class FirstInFirstOutStrategy implements TaskSelectionStrategy {
	
	/**
	 * キューからタスクを選ぶ
	 * （キューの先頭から選ぶ）
	 * @param agent
	 * @return
	 */
	@Override
	public Task selectTask(Agent agent){
		for(Task task : TeamFormationInstances.getInstance().getParameter().lookingTaskQueue()){
			if(!task.getMark()){
				if(EstimationLibrary.canExecuteTaskInTeam(agent, task)){
					return task;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * キューソートの方法を返す
	 */
	public String toString(){
		return "FIFO + 見積もりあり";
	}

}
