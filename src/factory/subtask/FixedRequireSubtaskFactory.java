package factory.subtask;

import constant.Constant;
import task.Subtask;

public class FixedRequireSubtaskFactory implements SubtaskFactory {

	/**
	 * サブタスクリソースを固定して生成する
	 * @param deadline
	 * @return
	 */
	@Override
	public Subtask makeSubtask(int deadline) {
		int[] require = new int[Constant.RESOURCE_NUM];
		for(int i = 0; i < require.length; i++){
			require[i] = Constant.TASK_REQUIRE_MALTIPLE * Constant.TASK_DEADLINE_MULTIPLE * Constant.TASK_REQUIRE_FIXED_VALUE;
		}
		return new Subtask(require, deadline);
	}

	/**
	 * サブタスクリソースを固定して返す
	 * @return
	 */
	@Override
	public int[] getSubtaskRequire() {
		int[] require = new int[Constant.RESOURCE_NUM];
		for(int i = 0; i < require.length; i++){
			require[i] = Constant.TASK_REQUIRE_MALTIPLE * Constant.TASK_DEADLINE_MULTIPLE * Constant.TASK_REQUIRE_FIXED_VALUE;
		}
		return require;
	}

	public String toString() {
		return "リソース固定してサブタスクを生成するfactory";
	}

}
