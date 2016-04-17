package role;


public class MemberMeasuredData extends MeasuredDataForEachRole {
	
	private int memberNum = 0;
	private int memberStateTime = 0;
	
	@Override
	public void countRoleNum() {
		memberNum++;
	}
	
	@Override
	public int getRoleNum() {
		return memberNum;
	}

	@Override
	public void countStateTime() {
		memberStateTime++;
	}
	
	@Override
	public int getStateTime() {
		return memberStateTime;
	}

	@Override
	public Role getRoleType() {
		return Role.MEMBER;
	}

	@Override
	public String toString() {
		return "Member";
	}

}
