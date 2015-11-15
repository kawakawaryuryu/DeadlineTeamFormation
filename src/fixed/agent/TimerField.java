package fixed.agent;

public class TimerField {

	int taskMarkedWaitingStateTimer = 0;
	int taskUnmarkedWaitingStateTimer = 0;
	
	int taskExecuteStateTimer = 0;
	
	
	public void initialize() {
		taskMarkedWaitingStateTimer = 0;
		taskUnmarkedWaitingStateTimer = 0;
		taskExecuteStateTimer = 0;
	}
	
	public void countTaskMarkedWaitingStateTimer() {
		taskMarkedWaitingStateTimer++;
	}
	
	public void countTaskUnmarkedWaitingStateTimer() {
		taskUnmarkedWaitingStateTimer++;
	}
	
	public void countTaskExecuteStateTimer() {
		taskExecuteStateTimer++;
	}
	
	public int getTaskMarkedWaitingStateTimer() {
		return taskMarkedWaitingStateTimer;
	}
	
	public int getTaskUnmarkedWaitingStateTimer() {
		return taskUnmarkedWaitingStateTimer;
	}
	
	public int getTaskExecuteStateTimer() {
		return taskExecuteStateTimer;
	}
}
