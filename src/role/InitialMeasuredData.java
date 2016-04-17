package role;


public class InitialMeasuredData extends MeasuredDataForEachRole {
	
	private int initialStateTime = 0;

	@Override
	public void countStateTime() {
		initialStateTime++;
	}
	
	@Override
	public int getStateTime() {
		return initialStateTime;
	}

	@Override
	public Role getRoleType() {
		return Role.INITIAL;
	}

	@Override
	public String toString() {
		return "Initial";
	}

}
