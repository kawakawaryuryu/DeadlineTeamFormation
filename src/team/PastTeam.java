package team;

import java.util.ArrayList;

import constant.Constant;

public class PastTeam {
	private ArrayList<Team> pastTeams = new ArrayList<Team>();
	private double averageAbilitiesPerTeam;
	
	public void addTeam(Team team) {
		pastTeams.add(team);
		if(pastTeams.size() > Constant.PAST_TEAM_NUM){
			pastTeams.remove(0);
		}
		
		updateAverageTeamAbilities();
	}
	
	private void updateAverageTeamAbilities() {
		double averageAbilitiesSum = 0.0;
		for(Team team : pastTeams){
			averageAbilitiesSum += team.getTeamResourceSum();
		}
		
		if(pastTeams.size() != 0){
			averageAbilitiesPerTeam = averageAbilitiesSum / pastTeams.size();
		}
	}
	
	public double getAverageAbilitiesPerTeam() {
		return averageAbilitiesPerTeam;
	}
	
	public boolean isEmptyPastTeams() {
		return pastTeams.isEmpty();
	}
}
