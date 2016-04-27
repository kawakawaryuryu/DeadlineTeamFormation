package state;

import strategy.StrategyManager;
import strategy.taskselection.TaskSelectionStrategy;
import task.Failure;
import task.Task;
import log.Log;
import main.TeamFormationMain;
import agent.Agent;

public class TaskSelectionState implements State {
	
	private static State state = new TaskSelectionState();
	private TaskSelectionStrategy strategy = StrategyManager.getTaskSelectionStrategy();

	@Override
	public void agentAction(Agent agent) {
		
		Task selectedTask = strategy.selectTask(agent);
		if(selectedTask != null){
			Log.log.debugln(selectedTask + "をマークしました");
			selectedTask.markingTask(true, Failure.MARK_TURE);
			agent.getParameter().setMarkedTask(selectedTask);
			
			// マークしたタスクに関するデータを計測
			TeamFormationMain.getMeasure().countMarkedTask(selectedTask);
		}
		else{
			Log.log.debugln("タスクをマークしませんでした");
		}
		
		agent.getParameter().changeState(RoleSelectionState.getState());
		TeamFormationMain.getParameter().addAgentToAgentsMap(RoleSelectionState.getState(), agent);
	}

	public static State getState() {
		return state;
	}
	
	public String getStrategy() {
		return strategy.toString();
	}
	
	public String toString() {
		return "タスク選択状態";
	}

}
