package action;

import main.teamformation.TeamFormationInstances;
import agent.Agent;

/**
 * タスクを返却するのにキューの最後に戻す 
 */
public class TaskReturnToLastWaitingAction implements Action {


	@Override
	public void action(Agent agent) {

		// 現時点はタスクはマークし、チーム編成に成功した場合初めてキューから取り除くので、
		// キューの最後に戻すのは一旦キューから削除し、再度キューの最後に追加するようにしている
		TeamFormationInstances.getInstance().getParameter().removeTask(agent.getParameter().getMarkedTask());
		TeamFormationInstances.getInstance().getParameter().returnTask(agent.getParameter().getMarkedTask());
	}

	public String toString() {
		return "キューの最後にタスクを返却";
	}

}
