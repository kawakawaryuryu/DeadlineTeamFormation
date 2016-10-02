package action;

import agent.Agent;

/**
 * タスクを返却するのにマークを外すだけに時間がかかる 
 */
public class TaskReturnUnmarkedWaitingAction implements Action {


	@Override
	public void action(Agent agent) {
		// 特にすることはない

	}

	public String toString() {
		return "キューのそのままの位置にタスクを戻す(マーク外すだけ)";
	}

}
