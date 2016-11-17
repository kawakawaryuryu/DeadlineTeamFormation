package factory.task;

import constant.Constant;
import random.RandomKey;
import random.RandomManager;
import task.Task;

public class RandomTaskFactory implements TaskFactory {

	/**
	 * サブタスク数、デッドラインともにランダムに与えてタスクを生成する
	 * @param id
	 * @return
	 */
	@Override
	public Task makeTask(int id) {
		// サブタスク数
		int numberOfSubtask = RandomManager.getRandom(RandomKey.TASK_RANDOM).nextInt(Constant.SUBTASK_IN_TASK_NUM) + Constant.SUBTASK_IN_TASK_INIT;

		// デッドライン
		int deadline = Constant.TASK_DEADLINE_MULTIPLE * (RandomManager.getRandom(RandomKey.DEADLINE_RANDOM).nextInt(Constant.DEADLINE_MAX) + Constant.DEADLINE_INIT);

		return new Task(id, numberOfSubtask, deadline);
	}

}
