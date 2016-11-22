package factory.subtask;

import constant.Constant;
import random.RandomKey;
import random.RandomManager;
import task.Subtask;

public class RandomSubtaskFactory implements SubtaskFactory {

	/**
	 * サブタスクリソースをランダムに与えて生成する
	 * @param deadline
	 * @return
	 */
	@Override
	public Subtask makeSubtask(int deadline) {
		int[] require = new int[Constant.RESOURCE_NUM];
		for(int i = 0; i < require.length; i++){
			require[i] = Constant.TASK_REQUIRE_MALTIPLE * Constant.TASK_DEADLINE_MULTIPLE
					* (RandomManager.getRandom(RandomKey.REQUIRE_RANDOM).nextInt(Constant.TASK_REQUIRE_MAX) + Constant.TASK_REQUIRE_INIT);
		}
		return new Subtask(require, deadline);
	}

	/**
	 * ランダムに与えたサブタスクリソースを返す
	 * @return
	 */
	@Override
	public int[] getSubtaskRequire() {
		int[] require = new int[Constant.RESOURCE_NUM];
		for(int i = 0; i < require.length; i++){
			require[i] = Constant.TASK_REQUIRE_MALTIPLE * Constant.TASK_DEADLINE_MULTIPLE
					* (RandomManager.getRandom(RandomKey.REQUIRE_RANDOM).nextInt(Constant.TASK_REQUIRE_MAX) + Constant.TASK_REQUIRE_INIT);
		}
		return require;
	}
}
