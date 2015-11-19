package fixed.state;

import fixed.agent.FixedAgent;
import fixed.main.TeamFormationMain;
import fixed.strategy.StrategyManager;
import fixed.strategy.taskselection.FixedTaskSelectionStrategy;
import fixed.task.FixedTask;

public class TaskSelectionState implements FixedState {
	
	private static FixedState state = new TaskSelectionState();
	private FixedTaskSelectionStrategy strategy = StrategyManager.getTaskSelectionStrategy();

	@Override
	public void agentAction(FixedAgent agent) {
		
		FixedTask selectedTask = strategy.selectTask(agent);
		if(selectedTask != null){
//			System.out.println(selectedTask + "をマークしました");
			selectedTask.markingTask(true);
			agent.getParameter().setMarkedTask(selectedTask);
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
