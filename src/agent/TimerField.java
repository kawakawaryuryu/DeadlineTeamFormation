package agent;

public class TimerField {

	int taskMarkedWaitingStateTimer = 0;
	int taskReturnedWaitingStateTimer = 0;

	int leaderWaitingStateTimer = 0;
	int memberWaitingStateTimer = 0;
	
	int taskExecuteStateTimer = 0;
	
	
	public void initialize() {
		taskMarkedWaitingStateTimer = 0;
		taskReturnedWaitingStateTimer = 0;
		leaderWaitingStateTimer = 0;
		memberWaitingStateTimer = 0;
		taskExecuteStateTimer = 0;
	}
	
	public void countTaskMarkedWaitingStateTimer() {
		taskMarkedWaitingStateTimer++;
	}
	
	public void countTaskReturnedWaitingStateTimer() {
		taskReturnedWaitingStateTimer++;
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
	
	public int getTaskReturnedWaitingStateTimer() {
		return taskReturnedWaitingStateTimer;
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
