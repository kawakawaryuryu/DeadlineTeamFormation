package fixed.role;


public class ExecuteMeasuredData extends MeasuredDataForEachRole {
	
	private int executeStateTime = 0;

	@Override
	public void countStateTime() {
		executeStateTime++;
	}
	
	@Override
	public int getStateTime() {
		return executeStateTime;
	}

	@Override
	public Role getRoleType() {
		return Role.EXECUTE;
	}

	@Override
	public String toString() {
		return "Execute";
	}

}
