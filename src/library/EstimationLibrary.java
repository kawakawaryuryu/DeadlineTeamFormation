package library;

import agent.Agent;
import constant.Constant;
import log.Log;
import task.Task;

public class EstimationLibrary {

	/**
	 * チームでタスクをデッドラインまでの処理できるか見積もる
	 * @param agent
	 * @param task
	 * @return
	 */
	public static boolean canExecuteTaskInTeam(Agent agent, Task task){
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
}
