package factory.task;

import constant.Constant;
import random.RandomKey;
import random.RandomManager;
import task.Task;

public class FixedSubtaskNumTaskFactory implements TaskFactory {

	/**
	 * サブタスク数を固定値にして生成する
	 */
	@Override
	public Task makeTask(int id) {
		// サブタスク数
		int numberOfSubtask = Constant.SUBTASK_IN_TASK_FIXED_NUM;

		// デッドライン
		int deadline = Constant.TASK_DEADLINE_MULTIPLE * (RandomManager.getRandom(RandomKey.DEADLINE_RANDOM).nextInt(Constant.DEADLINE_MAX) + Constant.DEADLINE_INIT);

		return new Task(id, numberOfSubtask, deadline);
	}

	public String toString() {
		return "サブタスク数を固定してタスクを生成するfactory";
	}

}
