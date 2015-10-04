package initialMarkingTask;

import main.Constant;
import main.Main;
import task.Task;
import agent.Agent;
import team.Team;

/**
 * キューはそのまま並び替えない
 * （タスク選択はキューの先頭から選ぶ）
 */
public class FirstInFirstOutStrategy implements InitialMarkingTaskStrategy {
//	private static Random sort_random1 = new Random(1007);	//1007ソートのランダムインスタンス
//	private static Random epsilon_greedy_random1 = new Random(101);	//101
//	private static Random epsilon_greedy_random2 = new Random(103);	//103
//	private static Random shuffle_random1 = new Random(107);	//107
//	private static Random select_random1 = new Random(109);	//109
//	private static Random select_random2 = new Random(113);	//113
//	private static Random select_random3 = new Random(127);	//127
	
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
//			for(Team past : agent.getPastTeams()){
//				System.out.println(past);
//			}
//			System.out.println("チーム履歴の平均リソース：" + agent.getAverageTeamResource());
			
			//ただのチーム平均リソースの場合
			countTime = (int)Math.ceil((double)leftTaskResource / (double)agent.getAverageTeamResource());
			//１人あたりの平均リソースの場合
//			countTime = (int)Math.ceil((double)leftTaskResource / ((double)agent.getAverageTeamResource() * (double)task.getSubTaskNum()));
			
//			System.out.println("チーム履歴 " + agent.getAverageTeamResource() + " がタスク" + task + "の処理にかかる時間 = " + countTime);
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
		return "FIFO + 見積もりあり";
	}

}
