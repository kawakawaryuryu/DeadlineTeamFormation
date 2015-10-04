package fixed.state;

import fixed.agent.FixedAgent;
import fixed.task.FixedSubtask;

public class TaskExecuteState implements FixedState {
	
	private static FixedState state = new TaskExecuteState();

	@Override
	public void agentAction(FixedAgent agent) {
		agent.getParameter().countTaskExecuteStateTimer();
//		debugExecuteTime(agent);
		
		if(agent.getParameter().getTaskExecuteStateTimer() <= agent.getParameter().getExecuteTime() &&
				agent.getParameter().getTaskExecuteStateTimer() != agent.getParameter().getParticipatingTeam().getTeamExecuteTime()){
//			debugExecutedSubtask(agent);
		}
		else if(agent.getParameter().getExecuteTime() < agent.getParameter().getTaskExecuteStateTimer() &&
				agent.getParameter().getTaskExecuteStateTimer() < agent.getParameter().getParticipatingTeam().getTeamExecuteTime()){
//			System.out.println("他のメンバの処理終了待ちです");
		}
		else if(agent.getParameter().getTaskExecuteStateTimer() == agent.getParameter().getParticipatingTeam().getTeamExecuteTime()){
			agent.getParameter().changeState(TaskSelectionState.getState());
//			System.out.println("チームの処理が終了しました");
		}
		else{
			System.err.println("TaskExecuteStateでこのようなパターンはありえません");
			System.exit(-1);
		}

	}
	
	private void debugExecutedSubtask(FixedAgent agent) {
		System.out.println("以下のサブタスクを処理中です");
		for(FixedSubtask subtask : agent.getParameter().getExecutedSubtasks()){
			System.out.println(subtask);
		}
	}
	
	private void debugExecuteTime(FixedAgent agent) {
		System.out.println("タスク処理時間 = " + agent.getParameter().getExecuteTime());
		System.out.println("チーム処理時間 = " + agent.getParameter().getParticipatingTeam().getTeamExecuteTime());
		System.out.println("実行状態タイマー = " + agent.getParameter().getTaskExecuteStateTimer());
	}

	public static FixedState getState() {
		return state;
	}
	
	public String toString() {
		return "タスク実行状態";
	}

}
