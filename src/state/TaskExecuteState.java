package state;

import task.Subtask;
import agent.Agent;

public class TaskExecuteState implements State {
	
	private static State state = new TaskExecuteState();

	@Override
	public void agentAction(Agent agent) {
		agent.getParameter().getTimerField().countTaskExecuteStateTimer();
//		debugExecuteTime(agent);
		
		if(agent.getParameter().getTimerField().getTaskExecuteStateTimer() <= agent.getParameter().getExecuteTime() &&
				agent.getParameter().getTimerField().getTaskExecuteStateTimer() != agent.getParameter().getParticipatingTeam().getTeamExecuteTime()){
//			debugExecutedSubtask(agent);
		}
		else if(agent.getParameter().getExecuteTime() < agent.getParameter().getTimerField().getTaskExecuteStateTimer() &&
				agent.getParameter().getTimerField().getTaskExecuteStateTimer() < agent.getParameter().getParticipatingTeam().getTeamExecuteTime()){
//			System.out.println("他のメンバの処理終了待ちです");
		}
		else if(agent.getParameter().getTimerField().getTaskExecuteStateTimer() == agent.getParameter().getParticipatingTeam().getTeamExecuteTime()){
			agent.getParameter().changeState(TaskSelectionState.getState());
//			System.out.println("チームの処理が終了しました");
		}
		else{
			System.err.println("TaskExecuteStateでこのようなパターンはありえません");
			System.exit(-1);
		}

	}
	
	private void debugExecutedSubtask(Agent agent) {
		System.out.println("以下のサブタスクを処理中です");
		for(Subtask subtask : agent.getParameter().getExecutedSubtasks()){
			System.out.println(subtask);
		}
	}
	
	private void debugExecuteTime(Agent agent) {
		System.out.println("タスク処理時間 = " + agent.getParameter().getExecuteTime());
		System.out.println("チーム処理時間 = " + agent.getParameter().getParticipatingTeam().getTeamExecuteTime());
		System.out.println("実行状態タイマー = " + agent.getParameter().getTimerField().getTaskExecuteStateTimer());
	}

	public static State getState() {
		return state;
	}
	
	public String toString() {
		return "タスク実行状態";
	}

}
