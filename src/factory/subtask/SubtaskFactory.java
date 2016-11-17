package factory.subtask;

import task.Subtask;

public interface SubtaskFactory {

	public Subtask makeSubtask(int deadline);

	public int[] getSubtaskRequire();
}
