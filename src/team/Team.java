package team;

import java.util.ArrayList;

import constant.Constant;
import agent.ConcreteAgent;

public class Team {
	private int teamExecuteTime = 0;
	private int executingTime = 0;
	private double executingTimePerAgent = 0;
	private int bindingTime = 0;
	private double bindingTimePerAgent = 0;
	private int[] teamResource = new int[Constant.RESOURCE_NUM];
	private int teamResourceSum = 0;
	private ArrayList<ConcreteAgent> tentativeTeamMate = new ArrayList<ConcreteAgent>();
	private ArrayList<ConcreteAgent> teamMate = new ArrayList<ConcreteAgent>();
	private ConcreteAgent leader;
	private ArrayList<ConcreteAgent> members = new ArrayList<ConcreteAgent>();
	
	public Team(ConcreteAgent leader) {
		this.leader = leader;
		addTentativeTeamMate(leader);
		for(ConcreteAgent member : leader.getParameter().getSendAgents()){
			addTentativeTeamMate(member);
		}
	}
	
	public void addTentativeTeamMate(ConcreteAgent agent) {
		tentativeTeamMate.add(agent);
	}
	
	public void addTeamMate(ConcreteAgent agent) {
		teamMate.add(agent);
		setTeamResource(agent);
		setTeamExecuteTime(agent);
	}
	
	private void setTeamResource(ConcreteAgent agent) {
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			teamResource[i] += agent.getAbility(i);
		}
		teamResourceSum += agent.getAbilitySum();
	}
	
	private void setTeamExecuteTime(ConcreteAgent agent) {
		if(teamExecuteTime < agent.getParameter().getExecuteTime()){
			teamExecuteTime = agent.getParameter().getExecuteTime();
		}
	}
	
	public void addMember(ConcreteAgent agent) {
		members.add(agent);
		addTeamMate(agent);
	}
	
	public void calculateExecutingTime() {
		int executingTimeSum = 0;
		for(ConcreteAgent agent : teamMate){
			executingTimeSum += agent.getParameter().getExecuteTime();
		}
		executingTime = executingTimeSum;
		executingTimePerAgent = (double)executingTimeSum / (double)teamMate.size();
	}
	
	public void calculateBindingTime() {
		int bindingTimeSum = 0;
		for(ConcreteAgent agent : teamMate){
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
	
	public ConcreteAgent getLeader() {
		return leader;
	}
	
	public ArrayList<ConcreteAgent> getTentativeTeamMate() {
		return tentativeTeamMate;
	}
	
	public ArrayList<ConcreteAgent> getTeamMate() {
		return teamMate;
	}
	
	public ArrayList<ConcreteAgent> getMembers() {
		return members;
	}
	
	public int getLeaderResource() {
		return leader.getAbilitySum();
	}
	
	public double getMemberResourcePerAgent() {
		double memberResource = 0;
		for(ConcreteAgent member : members){
			memberResource += member.getAbilitySum();
		}
		
		return memberResource / (double)members.size();
	}

}
