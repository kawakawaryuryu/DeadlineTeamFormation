package agent;

public class TimerField {

	int taskMarkedWaitingStateTimer = 0;
	int taskUnmarkedWaitingStateTimer = 0;

	int leaderWaitingStateTimer = 0;
	int memberWaitingStateTimer = 0;
	
	int taskExecuteStateTimer = 0;
	
	
	public void initialize() {
		taskMarkedWaitingStateTimer = 0;
		taskUnmarkedWaitingStateTimer = 0;
		leaderWaitingStateTimer = 0;
		memberWaitingStateTimer = 0;
		taskExecuteStateTimer = 0;
	}
	
	public void countTaskMarkedWaitingStateTimer() {
		taskMarkedWaitingStateTimer++;
	}
	
	public void countTaskUnmarkedWaitingStateTimer() {
		taskUnmarkedWaitingStateTimer++;
	}

	public void countLeaderWaitingStateTimer() {
		leaderWaitingStateTimer++;
	}

	public void countMemberWaitingStateTimer() {
		memberWaitingStateTimer++;
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

	public int getLeaderWaitingStateTimer() {
		return leaderWaitingStateTimer;
	}

	public int getMemberWaitingStateTimer() {
		return memberWaitingStateTimer;
	}
	
	public int getTaskExecuteStateTimer() {
		return taskExecuteStateTimer;
	}
}
