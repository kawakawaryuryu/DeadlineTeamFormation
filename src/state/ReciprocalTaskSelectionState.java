package state;

import log.Log;
import main.teamformation.TeamFormationInstances;
import strategy.StrategyManager;
import strategy.taskselection.TaskSelectionStrategy;
import task.Task;
import action.ActionManager;
import agent.Agent;
import agent.RationalAgent;
import agent.StructuredAgent;

public class ReciprocalTaskSelectionState implements State {

	private static State state = new ReciprocalTaskSelectionState();
	private TaskSelectionStrategy strategy = StrategyManager.getTaskSelectionStrategy();

	@Override
	public void agentAction(Agent agent) {
		
		Task selectedTask = null;
		// エージェントが合理的エージェントもしくはチーム固定エージェントだが信頼エージェントを保持していないとき
		if (agent instanceof RationalAgent
				|| (agent instanceof StructuredAgent && agent.getParameter().getTrustLeaders().isEmpty())) {
			selectedTask = strategy.selectTask(agent);
		}
		if(selectedTask != null){
			Log.log.debugln(selectedTask + "をマークしました");
			selectedTask.markingTask(true);
			agent.getParameter().setMarkedTask(selectedTask);
			
			// マークしたタスクに関するデータを計測
			TeamFormationInstances.getInstance().getMeasure().countMarkedTask(selectedTask);
		}
		else{
			Log.log.debugln("タスクをマークしませんでした");
		}
		
		ActionManager.toRoleSelectionStateAction.action(agent);
	}

	public static State getState() {
		return state;
	}
	
	public String getStrategy() {
		return strategy.toString();
	}
	
	public String toString() {
		return "タスク選択状態（信頼エージェントを保持しているエージェントはタスクを選択しない）";
	}

}
