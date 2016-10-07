package strategy.taskreturn;

import main.teamformation.TeamFormationInstances;
import constant.Constant;
import exception.AbnormalException;
import task.Task;
import agent.Agent;

public class TaskDisposalStrategy implements TaskReturnStrategy {

	@Override
	public void returnTask(Agent agent) {
		// サブタスクが保持している情報をクリア
		agent.getParameter().getMarkedTask().clear();
		// タスクマークを外す
		agent.getParameter().getMarkedTask().markingTask(false);

		// チーム編成失敗によってタスクがキューに戻される回数がある一定値を超えたらタスクを廃棄する
		Task task = agent.getParameter().getMarkedTask();
		if (task.getRemovedMarkNumByTeamFormationFailure() == Constant.TASK_DISPOSAL_THREASHOLD) {
			TeamFormationInstances.getInstance().getParameter().removeTask(task);
		}
		else if (task.getRemovedMarkNumByTeamFormationFailure() > Constant.TASK_DISPOSAL_THREASHOLD) {
			throw new AbnormalException("チーム編成失敗によるタスクマークの外され回数がタスク廃棄の閾値を超えています");
		}
	}

	public String toString() {
		return "チーム編成失敗によってタスクを廃棄する";
	}

}
