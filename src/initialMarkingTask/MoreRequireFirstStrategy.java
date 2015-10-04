package initialMarkingTask;

import main.Constant;
import main.Main;
import task.Task;
import agent.Agent;

/**
 * 獲得報酬（リソース量）の多いタスクをキューから取っていく
 */
public class MoreRequireFirstStrategy implements InitialMarkingTaskStrategy {

	/**
	 * 獲得報酬（リソース量）の多いタスクから選択していく
	 * @param agent
	 * @return
	 */
	@Override
	public Task selectTask(Agent agent) {
		Main.sortTaskQueueByRequire();	//リソースの多い順に並べ替える
		for(Task task : Main.lookingTaskQueue()){
			if(task.getMark() == false){
				if(isExecutedTaskInTeam(agent, task)){
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
	public boolean isExecutedTaskInTeam(Agent agent, Task task){
		int countTime = 0;
		int leftTaskResource = task.getTaskRequireSum();
		
		/* 過去のチーム履歴がない場合はとりあえず処理できるとする */
		if(!agent.getPastTeams().isEmpty()){
//			System.out.println("最大のリソースを持つチーム：" + agent.getMaxResourceTeam());
			countTime = (int)Math.ceil((double)leftTaskResource / (double)agent.getAverageTeamResource());
		}
		else{
//			System.out.println("チーム履歴がありませんでした");
		}
		
		if(countTime <= task.getDeadlineInTask() - 2 * Constant.WAIT_TURN){
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
		return "MRF + 見積もりあり";
	}

}
