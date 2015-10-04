package fixed.team;

import java.util.ArrayList;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;

public class FixedTeam {
	private int teamExecuteTime = 0;
	private int executingTime = 0;
	private double executingTimePerAgent = 0;
	private int bindingTime = 0;
	private double bindingTimePerAgent = 0;
	private int[] teamResource = new int[FixedConstant.RESOURCE_NUM];
	private int teamResourceSum = 0;
	private ArrayList<FixedAgent> tentativeTeamMate = new ArrayList<FixedAgent>();
	private ArrayList<FixedAgent> teamMate = new ArrayList<FixedAgent>();
	private FixedAgent leader;
	private ArrayList<FixedAgent> members = new ArrayList<FixedAgent>();
	
	public FixedTeam(FixedAgent leader) {
		this.leader = leader;
		addTentativeTeamMate(leader);
	}
	
	public void addTentativeTeamMate(FixedAgent agent) {
		tentativeTeamMate.add(agent);
	}
	
	public void addTeamMate(FixedAgent agent) {
		teamMate.add(agent);
		setTeamResource(agent);
		setTeamExecuteTime(agent);
	}
	
	private void setTeamResource(FixedAgent agent) {
		for(int i = 0; i < FixedConstant.RESOURCE_NUM; i++){
			teamResource[i] += agent.getAbility(i);
		}
		teamResourceSum += agent.getAbilitySum();
	}
	
	private void setTeamExecuteTime(FixedAgent agent) {
		if(teamExecuteTime < agent.getParameter().getExecuteTime()){
			teamExecuteTime = agent.getParameter().getExecuteTime();
		}
	}
	
	public void addMember(FixedAgent agent) {
		members.add(agent);
		addTeamMate(agent);
	}
	
	public void calculateExecutingTime() {
		int executingTimeSum = 0;
		for(FixedAgent agent : teamMate){
			executingTimeSum += agent.getParameter().getExecuteTime();
		}
		executingTime = executingTimeSum;
		executingTimePerAgent = (double)executingTimeSum / (double)teamMate.size();
	}
	
	public void calculateBindingTime() {
		int bindingTimeSum = 0;
		for(FixedAgent agent : teamMate){
			bindingTimeSum += (teamExecuteTime - agent.getParameter().getExecuteTime());
		}
		bindingTime = bindingTimeSum;
		bindingTimePerAgent = (double)bindingTimeSum / (double)teamMate.size();
	}
	
	public int getTeamExecuteTime() {
		return teamExecuteTime;
	}
	
	public int getExecutingTime() {
		return executingTime;
	}
	
	public double getExecutingTimePerAgent() {
		return executingTimePerAgent;
	}
	
	public int getBindingTime() {
		return bindingTime;
	}
	
	public double getBindingTimePerAgent() {
		return bindingTimePerAgent;
	}
	
	public int getTeamResource(int i) {
		return teamResource[i];
	}
	
	public int getTeamResourceSum() {
		return teamResourceSum;
	}
	
	public FixedAgent getLeader() {
		return leader;
	}
	
	public ArrayList<FixedAgent> getTentativeTeamMate() {
		return tentativeTeamMate;
	}
	
	public ArrayList<FixedAgent> getTeamMate() {
		return teamMate;
	}
	
	public ArrayList<FixedAgent> getMembers() {
		return members;
	}
	
	public int getLeaderResource() {
		return leader.getAbilitySum();
	}
	
	public double getMemberResourcePerAgent() {
		double memberResource = 0;
		for(FixedAgent member : members){
			memberResource += member.getAbilitySum();
		}
		
		return memberResource / (double)members.size();
	}

}
