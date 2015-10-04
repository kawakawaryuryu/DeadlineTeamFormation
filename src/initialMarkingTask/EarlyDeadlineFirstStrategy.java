package initialMarkingTask;

import main.Constant;
import main.Main;
import task.Task;
import agent.Agent;

/**
 * キューからはデッドラインの早いタスクを選択する
 * リーダの期待報酬は全報酬×欲張り度
 * メンバの期待報酬はサブタスク×リーダの報酬期待度
 */
public class EarlyDeadlineFirstStrategy implements InitialMarkingTaskStrategy {
//	private static Random sort_random1 = new Random(1007);	//1007ソートのランダムインスタンス
//	private static Random epsilon_greedy_random1 = new Random(101);	//101
//	private static Random epsilon_greedy_random2 = new Random(103);	//103
//	private static Random shuffle_random1 = new Random(107);	//107
//	private static Random select_random1 = new Random(109);	//109
//	private static Random select_random2 = new Random(113);	//113
	
	/**
	 * キューからタスクを選ぶ
	 * （デッドラインの早いタスクから）
	 * @param agent
	 * @return
	 */
	@Override
	public Task selectTask(Agent agent){
		Main.sortTaskQueueByDeadline();	//デッドラインの早い順に並び替える
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
		return "EDF + 見積もりあり";
	}
}
