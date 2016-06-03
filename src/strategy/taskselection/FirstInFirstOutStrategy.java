package strategy.taskselection;

import task.Task;
import log.Log;
import main.teamformation.TeamFormationInstances;
import constant.Constant;
import agent.ConcreteAgent;

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
	public Task selectTask(ConcreteAgent agent){
		for(Task task : TeamFormationInstances.getInstance().getParameter().lookingTaskQueue()){
			if(!task.getMark()){
				if(canExecuteTaskInTeam(agent, task)){
					return task;
				}
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
	public boolean canExecuteTaskInTeam(ConcreteAgent agent, Task task){
		int countTime = 0;
		int leftTaskResource = task.getTaskRequireSum();
		
		/* 過去のチーム履歴がない場合はとりあえず処理できるとする */
		if(!agent.getParameter().getPastTeam().isEmptyPastTeams()){
			//ただのチーム平均リソースの場合
			countTime = (int)Math.ceil((double)leftTaskResource / (double)agent.getParameter().getPastTeam().getAverageAbilitiesPerTeam());
			
		}
		else{
			Log.log.debugln("チーム履歴がありませんでした");
		}
		
		if(countTime <= task.getDeadlineInTask() - (Constant.WAIT_TURN + Constant.DEADLINE_MIN_2)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * キューソートの方法を返す
	 */
	public String toString(){
		return "FIFO + 見積もりあり";
	}

}
