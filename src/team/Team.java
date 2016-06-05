package team;

import java.util.ArrayList;

import constant.Constant;
import agent.Agent;

public class Team {
	private int teamExecuteTime = 0;
	private int executingTime = 0;
	private double executingTimePerAgent = 0;
	private int bindingTime = 0;
	private double bindingTimePerAgent = 0;
	private int[] teamResource = new int[Constant.RESOURCE_NUM];
	private int teamResourceSum = 0;
	private ArrayList<Agent> tentativeTeamMate = new ArrayList<Agent>();
	private ArrayList<Agent> teamMate = new ArrayList<Agent>();
	private Agent leader;
	private ArrayList<Agent> members = new ArrayList<Agent>();
	
	public Team(Agent leader) {
		this.leader = leader;
		addTentativeTeamMate(leader);
		for(Agent member : leader.getParameter().getSendAgents()){
			addTentativeTeamMate(member);
		}
	}
	
	public void addTentativeTeamMate(Agent agent) {
		tentativeTeamMate.add(agent);
	}
	
	public void addTeamMate(Agent agent) {
		teamMate.add(agent);
		setTeamResource(agent);
		setTeamExecuteTime(agent);
	}
	
	private void setTeamResource(Agent agent) {
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			teamResource[i] += agent.getAbility(i);
		}
		teamResourceSum += agent.getAbilitySum();
	}
	
	private void setTeamExecuteTime(Agent agent) {
		if(teamExecuteTime < agent.getParameter().getExecuteTime()){
			teamExecuteTime = agent.getParameter().getExecuteTime();
		}
	}
	
	public void addMember(Agent agent) {
		members.add(agent);
		addTeamMate(agent);
	}
	
	public void calculateExecutingTime() {
		int executingTimeSum = 0;
		for(Agent agent : teamMate){
			executingTimeSum += agent.getParameter().getExecuteTime();
		}
		executingTime = executingTimeSum;
		executingTimePerAgent = (double)executingTimeSum / (double)teamMate.size();
	}
	
	public void calculateBindingTime() {
		int bindingTimeSum = 0;
		for(Agent agent : teamMate){
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
	
	public Agent getLeader() {
		return leader;
	}
	
	public ArrayList<Agent> getTentativeTeamMate() {
		return tentativeTeamMate;
	}
	
	public ArrayList<Agent> getTeamMate() {
		return teamMate;
	}
	
	public ArrayList<Agent> getMembers() {
		return members;
	}
	
	public int getLeaderResource() {
		return leader.getAbilitySum();
	}
	
	public double getMemberResourcePerAgent() {
		double memberResource = 0;
		for(Agent member : members){
			memberResource += member.getAbilitySum();
		}
		
		return memberResource / (double)members.size();
	}

}
