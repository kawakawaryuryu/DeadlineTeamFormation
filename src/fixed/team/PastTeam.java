package fixed.team;

import java.util.ArrayList;

import fixed.constant.FixedConstant;

public class PastTeam {
	private ArrayList<FixedTeam> pastTeams = new ArrayList<FixedTeam>();
	private double averageAbilitiesPerTeam;
	
	public void addTeam(FixedTeam team) {
		pastTeams.add(team);
		if(pastTeams.size() > FixedConstant.PAST_TEAM_NUM){
			pastTeams.remove(0);
		}
		
		updateAverageTeamAbilities();
	}
	
	private void updateAverageTeamAbilities() {
		double averageAbilitiesSum = 0.0;
		for(FixedTeam team : pastTeams){
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
