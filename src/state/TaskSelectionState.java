package state;

import strategy.StrategyManager;
import strategy.taskselection.FixedTaskSelectionStrategy;
import task.Failure;
import task.FixedTask;
import main.TeamFormationMain;
import agent.FixedAgent;

public class TaskSelectionState implements FixedState {
	
	private static FixedState state = new TaskSelectionState();
	private FixedTaskSelectionStrategy strategy = StrategyManager.getTaskSelectionStrategy();

	@Override
	public void agentAction(FixedAgent agent) {
		
		FixedTask selectedTask = strategy.selectTask(agent);
		if(selectedTask != null){
//			System.out.println(selectedTask + "をマークしました");
			selectedTask.markingTask(true, Failure.MARK_TURE);
			agent.getParameter().setMarkedTask(selectedTask);
			
			// マークしたタスクに関するデータを計測
			TeamFormationMain.getMeasure().countMarkedTask(selectedTask);
		}
		else{
//			System.out.println("タスクをマークしませんでした");
		}
		
		agent.getParameter().changeState(RoleSelectionState.getState());
		TeamFormationMain.getParameter().addAgentToAgentsMap(RoleSelectionState.getState(), agent);
	}

	public static FixedState getState() {
		return state;
	}
	
	public String getStrategy() {
		return strategy.toString();
	}
	
	public String toString() {
		return "タスク選択状態";
	}

}
