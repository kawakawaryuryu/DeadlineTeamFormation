package strategy.taskselection;

import task.Task;
import library.DeadlineLibrary;
import library.EstimationLibrary;
import log.Log;
import main.teamformation.TeamFormationInstances;
import constant.Constant;
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
	 * チームでタスクをデッドラインまでの処理できるか見積もる
	 * @param agent
	 * @param task
	 * @return
	 */
	public boolean canExecuteTaskInTeam(Agent agent, Task task){
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
		
		if(countTime <= task.getDeadlineInTask() - (DeadlineLibrary.getReducedDeadlineAtInitialTurn(Constant.MESSAGE_DELAY))){
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
