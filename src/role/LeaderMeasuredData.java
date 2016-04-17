package role;


public class LeaderMeasuredData extends MeasuredDataForEachRole {
	
	private int leaderNum = 0;
	private int leaderStateTime = 0;
	
	@Override
	public void countRoleNum() {
		leaderNum++;
	}
	
	@Override
	public int getRoleNum() {
		return leaderNum;
	}

	@Override
	public void countStateTime() {
		leaderStateTime++;
	}
	
	@Override
	public int getStateTime() {
		return leaderStateTime;
	}

	@Override
	public Role getRoleType() {
		return Role.LEADER;
	}
	
	@Override
	public String toString() {
		return "Leader";
	}
}
