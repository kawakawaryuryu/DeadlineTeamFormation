package fixed.strategy.taskselection;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.main.TeamFormationMain;
import fixed.task.FixedTask;

/**
 * キューはそのまま並び替えない
 * （タスク選択はキューの先頭から選ぶ）
 */
public class FixedFirstInFirstOutStrategy implements FixedTaskSelectionStrategy {
	
	/**
	 * キューからタスクを選ぶ
	 * （キューの先頭から選ぶ）
	 * @param agent
	 * @return
	 */
	@Override
	public FixedTask selectTask(FixedAgent agent){
		for(FixedTask task : TeamFormationMain.getParameter().lookingTaskQueue()){
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
	public boolean canExecuteTaskInTeam(FixedAgent agent, FixedTask task){
		int countTime = 0;
		int leftTaskResource = task.getTaskRequireSum();
		
		/* 過去のチーム履歴がない場合はとりあえず処理できるとする */
		if(!agent.getParameter().getPastTeam().isEmptyPastTeams()){
			//ただのチーム平均リソースの場合
			countTime = (int)Math.ceil((double)leftTaskResource / (double)agent.getParameter().getPastTeam().getAverageAbilitiesPerTeam());
			
		}
		else{
//			System.out.println("チーム履歴がありませんでした");
		}
		
		if(countTime <= task.getDeadlineInTask() - FixedConstant.DEADLINE_MIN_2){
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
