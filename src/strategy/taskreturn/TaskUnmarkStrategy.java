package strategy.taskreturn;

import agent.Agent;

public class TaskUnmarkStrategy implements TaskReturnStrategy {


	@Override
	public void returnTask(Agent agent) {

		// サブタスクが保持している情報をクリア
		agent.getParameter().getMarkedTask().clear();
		// タスクマークを外す
		agent.getParameter().getMarkedTask().markingTask(false);
	}

	public String toString() {
		return "タスクのマークを外すだけ";
	}

}
