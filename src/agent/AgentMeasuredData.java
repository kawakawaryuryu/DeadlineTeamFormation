package agent;

import java.util.ArrayList;
import java.util.Arrays;

import role.MeasuredDataForEachRole;
import team.Team;
import main.teamformation.TeamFormationMain;
import constant.Constant;

public class AgentMeasuredData {
	private int successNum = 0;
	
	int[] teamFormationNumWithLeader = new int[Constant.AGENT_NUM];
	int[] teamFormationNumWithMember = new int[Constant.AGENT_NUM];
	
	int successNumAtEnd = 0;
	int executingTimeAtEnd = 0;
	int bindingTimeAtEnd = 0;
	
	public AgentMeasuredData() {
		Arrays.fill(teamFormationNumWithLeader, 0);
		Arrays.fill(teamFormationNumWithMember, 0);
	}
	
	public void countInLeaderSuccessCase(MeasuredDataForEachRole element, Agent leader, Team team) {
		countSuccessNum(element);
		countTeamFormationWithMembers(team.getMembers());
		countExecutingTimeAtEnd(leader.getParameter().getExecuteTime());
		countBindingTimeAtEnd(team.getTeamExecuteTime() - leader.getParameter().getExecuteTime());
	}
	
	public void countInMemberSuccessCase(MeasuredDataForEachRole element, Agent member, Team team) {
		countSuccessNum(element);
		countTeamFormationNumWithLeader(team.getLeader());
		countExecutingTimeAtEnd(member.getParameter().getExecuteTime());
		countBindingTimeAtEnd(team.getTeamExecuteTime() - member.getParameter().getExecuteTime());
	}
	
	private void countSuccessNum(MeasuredDataForEachRole element) {
		successNum++;
		element.countRoleNum();
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.END_TURN_NUM_FOR_AVERAGE){
			successNumAtEnd++;
		}
	}
	
	public int getSuccessNum() {
		return successNum;
	}
	
	public int getRoleNum(MeasuredDataForEachRole element) {
		return element.getRoleNum();
	}
	
	private void countTeamFormationWithMembers(ArrayList<Agent> members) {
		for(Agent member : members){
			teamFormationNumWithMember[member.getId()]++;
		}
	}
	
	private void countTeamFormationNumWithLeader(Agent leader) {
		teamFormationNumWithLeader[leader.getId()]++;
	}
	
	public int getTeamFormationNumWithLeader(Agent leader) {
		return teamFormationNumWithLeader[leader.getId()];
	}
	
	public int getTeamFormationNumWithMember(Agent member) {
		return teamFormationNumWithMember[member.getId()];
	}
	
	public void countStateTime(MeasuredDataForEachRole element) {
		element.countStateTime();
	}
	
	public int getStateTime(MeasuredDataForEachRole element) {
		return element.getStateTime();
	}
	
	public void countExecutingTimeAtEnd(int time) {
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.END_TURN_NUM_FOR_AVERAGE){
			executingTimeAtEnd += time;
		}
	}
	
	public void countBindingTimeAtEnd(int time) {
		if(Constant.TURN_NUM - TeamFormationMain.getTurn() < Constant.END_TURN_NUM_FOR_AVERAGE){
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
