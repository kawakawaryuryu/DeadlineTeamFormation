package fixed.agent;

import java.util.ArrayList;
import java.util.Arrays;

import fixed.constant.FixedConstant;
import fixed.main.TeamFormationMain;
import fixed.role.MeasuredDataForEachRole;
import fixed.team.FixedTeam;

public class AgentMeasuredData {
	private int successNum = 0;
	
	int[] teamFormationNumWithLeader = new int[FixedConstant.AGENT_NUM];
	int[] teamFormationNumWithMember = new int[FixedConstant.AGENT_NUM];
	
	int successNumAtEnd = 0;
	int executingTimeAtEnd = 0;
	int bindingTimeAtEnd = 0;
	
	public AgentMeasuredData() {
		Arrays.fill(teamFormationNumWithLeader, 0);
		Arrays.fill(teamFormationNumWithMember, 0);
	}
	
	public void countInLeaderSuccessCase(MeasuredDataForEachRole element, FixedAgent leader, FixedTeam team) {
		countSuccessNum(element);
		countTeamFormationWithMembers(team.getMembers());
		countExecutingTimeAtEnd(leader.getParameter().getExecuteTime());
		countBindingTimeAtEnd(team.getTeamExecuteTime() - leader.getParameter().getExecuteTime());
	}
	
	public void countInMemberSuccessCase(MeasuredDataForEachRole element, FixedAgent member, FixedTeam team) {
		countSuccessNum(element);
		countTeamFormationNumWithLeader(team.getLeader());
		countExecutingTimeAtEnd(member.getParameter().getExecuteTime());
		countBindingTimeAtEnd(team.getTeamExecuteTime() - member.getParameter().getExecuteTime());
	}
	
	private void countSuccessNum(MeasuredDataForEachRole element) {
		successNum++;
		element.countRoleNum();
		if(FixedConstant.TURN_NUM - TeamFormationMain.getTurn() < FixedConstant.MEASURE_SUCCESS_AT_END_TURN_NUM){
			successNumAtEnd++;
		}
	}
	
	public int getSuccessNum() {
		return successNum;
	}
	
	public int getRoleNum(MeasuredDataForEachRole element) {
		return element.getRoleNum();
	}
	
	private void countTeamFormationWithMembers(ArrayList<FixedAgent> members) {
		for(FixedAgent member : members){
			teamFormationNumWithMember[member.getId()]++;
		}
	}
	
	private void countTeamFormationNumWithLeader(FixedAgent leader) {
		teamFormationNumWithLeader[leader.getId()]++;
	}
	
	public int getTeamFormationNumWithLeader(FixedAgent leader) {
		return teamFormationNumWithLeader[leader.getId()];
	}
	
	public int getTeamFormationNumWithMember(FixedAgent member) {
		return teamFormationNumWithMember[member.getId()];
	}
	
	public void countStateTime(MeasuredDataForEachRole element) {
		element.countStateTime();
	}
	
	public int getStateTime(MeasuredDataForEachRole element) {
		return element.getStateTime();
	}
	
	public void countExecutingTimeAtEnd(int time) {
		if(FixedConstant.TURN_NUM - TeamFormationMain.getTurn() < FixedConstant.MEASURE_SUCCESS_AT_END_TURN_NUM){
			executingTimeAtEnd += time;
		}
	}
	
	public void countBindingTimeAtEnd(int time) {
		if(FixedConstant.TURN_NUM - TeamFormationMain.getTurn() < FixedConstant.MEASURE_SUCCESS_AT_END_TURN_NUM){
			bindingTimeAtEnd += time;
		}
	}
	
	/**
	 * 割る数が0にならないようにする
	 * @param num
	 * @return
	 */
	private double getDivideNum(int num) {
		if(num == 0){
			return 1;
		}
		else{
			return (double)num;
		}
	}
	
	public int getSuccessNumAtEnd() {
		return successNumAtEnd;
	}
	
	public double getAverageExecutingTimeAtEnd() {
		return (double)executingTimeAtEnd / getDivideNum(successNumAtEnd);
	}
	
	public double getAverageBindingTimeAtEnd() {
		return (double)bindingTimeAtEnd / getDivideNum(successNumAtEnd);
	}
}
